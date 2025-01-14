<%@ include file="header.jsp" %>

<html>
<head>
<title>Saving to the database</title>
<link rel="stylesheet" href="../jsp/css/style.css">
</head>
<%-- <%@ include file="body.jsp" %> --%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.util.DateUtils"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.constants.TransactionConstants"%>
<body>

<form name = "form1">
<jsp:useBean id="misc"  class="com.track.gates.miscBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sb"  class="com.track.gates.sqlBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="tl"  class="com.track.gates.TableList" />
<jsp:useBean id="df"  class="com.track.gates.defaultsBean" />

<%
// Getting the parameters of USER_INFO from external file
   
    session = request.getSession();
    String dept ="";
    String remarks="",selplant="";boolean  inserted=false;
    String plant = ((String)session.getAttribute("PLANT")).trim();
   
   selplant=plant;
     dept = request.getParameter("DEPT").trim();
	DateUtils dateutils = new DateUtils();
     ub.setmLogger(mLogger);
     df.setmLogger(mLogger);
     sb.setmLogger(mLogger);
    
    String action  = request.getParameter("Submit").trim();
    String user_id = request.getParameter("USER_ID");
   // String selecteduserid = request.getParameter("USER_ID1");
   String selecteduserid = request.getParameter("USER_ID1");
    ArrayList alUserInfo  = tl.getTableArray("USER_INFO");
    String ISADMIN= StrUtils.fString(request.getParameter("isApprovalAdmin"));
    if(ISADMIN.equalsIgnoreCase("on")){
    	ISADMIN ="1";
    }else{
    	ISADMIN ="0";
    }
    String ISPURCHASEAPPROVAL= StrUtils.fString(request.getParameter("isPurchaseApproval"));
    if(ISPURCHASEAPPROVAL.equalsIgnoreCase("on")){
    	ISPURCHASEAPPROVAL ="1";
    }else{
    	ISPURCHASEAPPROVAL ="0";
    }
    String ISSALESAPPROVAL= StrUtils.fString(request.getParameter("isSalesApproval"));
    if(ISSALESAPPROVAL.equalsIgnoreCase("on")){
    	ISSALESAPPROVAL ="1";
    }else{
    	ISSALESAPPROVAL ="0";
    }
    String ISPURCHASERETAPPROVAL= StrUtils.fString(request.getParameter("isPurchaseRetApproval"));
    if(ISPURCHASERETAPPROVAL.equalsIgnoreCase("on")){
    	ISPURCHASERETAPPROVAL ="1";
    }else{
    	ISPURCHASERETAPPROVAL ="0";
    }
    String ISSALESRETAPPROVAL= StrUtils.fString(request.getParameter("isSalesRetApproval"));
    if(ISSALESRETAPPROVAL.equalsIgnoreCase("on")){
    	ISSALESRETAPPROVAL ="1";
    }else{
    	ISSALESRETAPPROVAL ="0";
    }
    String WEB_ACCESS="",ISACCESSOWNERAPP="",MANAGER_APP_ACCESS="",ISACCESS_STOREAPP="",RIDER_APP_ACCESS="";
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		
		if ("W".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEB_ACCESS = "1";
			ISACCESSOWNERAPP = "0";
			MANAGER_APP_ACCESS = "0";
			ISACCESS_STOREAPP = "0";
			RIDER_APP_ACCESS = "0";
		}
		else if ("O".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEB_ACCESS = "0";
			ISACCESSOWNERAPP = "1";
			MANAGER_APP_ACCESS = "0";
			ISACCESS_STOREAPP = "0";
			RIDER_APP_ACCESS = "0";
		}
		else if ("M".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEB_ACCESS = "0";
			ISACCESSOWNERAPP = "0";
			MANAGER_APP_ACCESS = "1";
			ISACCESS_STOREAPP = "0";
			RIDER_APP_ACCESS = "0";
		}
		else if ("S".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEB_ACCESS = "0";
			ISACCESSOWNERAPP = "0";
			MANAGER_APP_ACCESS = "0";
			ISACCESS_STOREAPP = "1";
			RIDER_APP_ACCESS = "0";
		}
		else  {
			WEB_ACCESS = "0";
			ISACCESSOWNERAPP = "0";
			MANAGER_APP_ACCESS = "0";
			ISACCESS_STOREAPP = "0";
			RIDER_APP_ACCESS = "1";
		}
	}
	
    
   // System.out.println("Selected userid"+selecteduserid+"updated id"+user_id);
    ArrayList ralUserInfo = new ArrayList();
    String result ="", sql="";       int n=0;
    String accessCtr="";
   
    if(action.equalsIgnoreCase("delete"))
    {
     int k=0;
     sql = ub.getDeleteUserString(selecteduserid,dept);
   
      k = sb.deleteRecords(sql);
      System.out.println("selected userid"+sql+k);
    
   
     if(k==1)  {
     
     result = "<font class='maingreen'>The "+selecteduserid+" User ID has been successfully deleted.</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";
                      
                      } 
     else  {   result = "<font  class='mainred'> Error in deleting..Could not delete User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";
                        }                    

              df.insertToLog(session, "Deleting User ID", result);   //  Inserting into the user log
%>
       <jsp:forward page="displayResult.jsp" >
		<jsp:param name="RESULT" value="<%=result%>" />
         </jsp:forward> 
<%
    }

// Getting the result list size as main list - 8 eliminating au_by/on, up_by/on, en_by/on, access_counter,user_status
    // System.out.println("array length"+alUserInfo.size());
    int arrlength = alUserInfo.size() - 18;
   
    for(int i=0; i < arrlength; i++)
    {
        String str = request.getParameter((String)alUserInfo.get(i));
        remarks = request.getParameter((String)alUserInfo.get(4));
        ralUserInfo.add(str);

    }
    String USER_LEVEL_ACCOUNTING  = request.getParameter("USER_LEVEL_ACCOUNTING");
    String USER_LEVEL_PAYROLL  = request.getParameter("USER_LEVEL_PAYROLL");
    
    String pwd       = request.getParameter("PASSWORD");
    String user_name = request.getParameter("USER_NAME").trim().toUpperCase();
    String effDt     = request.getParameter("EFFECTIVE_DATE");
    String encrflag  = request.getParameter("ENCRYPT_FLAG");
    accessCtr = "0";

    if(encrflag.equalsIgnoreCase("1"))
    {
        pwd          = eb.encrypt(pwd);             //  Encrypting the password
       
       // accessCtr    = "C";
    }
  
    if ((effDt == null ) || (effDt.length()< 8)) effDt = gn.getDate();
    effDt            = gn.getDBDateShort(effDt);
     boolean b = misc.isValid(effDt,"0");    //  Check if the effective date is valid

        if(b == false)
        {
            String redirect =  " Error Occurred ..!! Effective Date can not be less than current date <br><br><center>";
                              // "<input type=button value=Back onClick=window.location.href='javascript:history.back()'>";
            df.insertToLog(session, "Updating User User ID", redirect);

//response.sendRedirect("displayResult.jsp?RESULT="+redirect);
%>
<input type = "hidden" name = "RESULT" value="<%=redirect%>">
<script language="JavaScript">
    document.form1.action = "../jsp/displayResult.jsp";
    document.form1.submit();

</script>

<%
        }
String loginuser=(String)session.getAttribute("LOGIN_USER");
    ralUserInfo.set(1,pwd);   //    Setting Encrypted password
    ralUserInfo.set(2,user_name);   //    Capitalizing the User Name
    ralUserInfo.set(5,effDt); 
    ralUserInfo.set(7,dept);
     accessCtr="0";
    //ralUserInfo.add(accessCtr);    //     Access Counter
    //ralUserInfo.add("0");    //     User Status
//    ralUserInfo.add(plant);
    String enrolledBy,enrolledOn,updatedBy,updatedOn;
    enrolledBy = (String)session.getAttribute("LOGIN_USER");
    enrolledOn = gn.getDateTime();
    updatedBy  = enrolledBy;
    updatedOn  = enrolledOn;

    if("save".equalsIgnoreCase(action))
    {
//      Add  enrolled_by/on    and   updated_by/on
      if(plant.equalsIgnoreCase("track"))
      {
        plant=dept;
      }
      //Code Comment by azees - Feb2023 Validation done createNewUser Page
      /* int i=0;
      i=ub.getUserCount(dept);
      if(i>11)
      {//Could not create the user. Reached maxium number of users. Please contact Scan2Track admin.
    	  result = "<font  class=mainred> Could not create user - "+user_id+". Reached maxium number of users. Please contact SUNPRO admin.<br><br><center>"+
                  "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                  "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      }
      else{ */
    	
	    //ralUserInfo.add(enrolledBy);    ralUserInfo.add(enrolledOn);
	    ralUserInfo.add(updatedBy);     ralUserInfo.add(updatedOn);
	    ralUserInfo.add(updatedBy); ralUserInfo.add(updatedOn); //      Setting the Authorisation to null
	    ralUserInfo.add(USER_LEVEL_ACCOUNTING);
	    ralUserInfo.add(USER_LEVEL_PAYROLL);
	    ralUserInfo.add(WEB_ACCESS);
	    ralUserInfo.add(ISACCESSOWNERAPP);
	    ralUserInfo.add(MANAGER_APP_ACCESS);
	    ralUserInfo.add(ISACCESS_STOREAPP);
	    ralUserInfo.add(RIDER_APP_ACCESS);
	    ralUserInfo.add(ISADMIN);
	    ralUserInfo.add(ISPURCHASEAPPROVAL);
	    ralUserInfo.add(ISSALESAPPROVAL);
	    ralUserInfo.add(ISPURCHASERETAPPROVAL);
	    ralUserInfo.add(ISSALESRETAPPROVAL);
	    ralUserInfo.add("0");//ISACCESSSUPERVISORAPP
	    ralUserInfo.add("0");//ISACCESSPROJECTMANAGERAPP

        if(!ub.isAlreadyAvail(user_id,0,plant))
        {
         //System.out.println("insert user"+tl.getFieldString(alUserInfo));
         //System.out.println("insert user"+tl.getValueString(alUserInfo));
         sql = "insert into USER_INFO("+tl.getFieldString(alUserInfo)+") values ("+tl.getValueString(ralUserInfo)+")";
         n = sb.insertRecords(sql);
         MovHisDAO mdao = new MovHisDAO();
	        mdao.setmLogger(mLogger);
	        Hashtable htm = new Hashtable();
	        htm.put(IDBConstants.PLANT,plant);
	        htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_USER);
	        htm.put("RECID","");
	        htm.put(IDBConstants.CREATED_BY,loginuser);  htm.put(IDBConstants.REMARKS,user_id+","+remarks);  
	        htm.put(IDBConstants.UPDATED_BY,loginuser);  
	        htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	        htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
	        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	        if(n==1)
	        {
	          inserted = mdao.insertIntodefaultMovHis(htm);
	        }
         if(n==1&& inserted== true) result = "<font class=maingreen>The  User ID "+user_id+" has been successfully created.</font><br><br><center>"+
                           "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";

         else     result = "<font  class=mainred> Could not add User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

        }
        else result =  "<font  class=mainred>User ID already available..Please Choose a different User ID <br>  Could not add User - "+user_id+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

              df.insertToLog(session, "Adding User ID", result);   //  Inserting into the user log

    //}
      }
    else if("update".equalsIgnoreCase(action))
    {
    
    String user=request.getParameter("USER_ID");
 
    ralUserInfo.set(0,user);
    accessCtr="0";
    //ralUserInfo.add(accessCtr);
    //ralUserInfo.add(updatedBy);
    //ralUserInfo.add(updatedOn);
    ralUserInfo.add(updatedBy);
    ralUserInfo.add(updatedOn);
    ralUserInfo.add(updatedBy); 
    ralUserInfo.add(updatedOn); 
    ralUserInfo.add(USER_LEVEL_ACCOUNTING);
    ralUserInfo.add(USER_LEVEL_PAYROLL);
    ralUserInfo.add(WEB_ACCESS);
    ralUserInfo.add(ISACCESSOWNERAPP);
    ralUserInfo.add(MANAGER_APP_ACCESS);
    ralUserInfo.add(ISACCESS_STOREAPP);
    ralUserInfo.add(RIDER_APP_ACCESS);
    ralUserInfo.add(ISADMIN);
    ralUserInfo.add(ISPURCHASEAPPROVAL);
    ralUserInfo.add(ISSALESAPPROVAL);
    ralUserInfo.add(ISPURCHASERETAPPROVAL);
    ralUserInfo.add(ISSALESRETAPPROVAL);
    MovHisDAO movdao =null;
    //      Setting the Authorisation to null
    if(selplant.equalsIgnoreCase("track"))
   	  movdao = new MovHisDAO();
    else
                movdao = new MovHisDAO(plant);
                movdao.setmLogger(mLogger);
	        Hashtable htm = new Hashtable();
	        htm.put(IDBConstants.PLANT,plant);
	        htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPDATE_USER);
	        htm.put("RECID","");
	        
	        htm.put(IDBConstants.CREATED_BY,loginuser);  
	        htm.put(IDBConstants.REMARKS,user_id+","+remarks);  
	        if(!remarks.equals(""))
	        {
	        	 htm.put(IDBConstants.REMARKS,user_id+","+remarks);  
	        }
	        else
	        {
	        	 htm.put(IDBConstants.REMARKS,user_id);  
	        }
	        htm.put(IDBConstants.UPBY,loginuser);  
	        htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	        htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
	        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		    sql = "update USER_INFO set "+tl.buildUpdateString(alUserInfo,ralUserInfo)+" where USER_ID='"+selecteduserid+"'"+"and dept='"+dept+"'";
        // System.out.println("Update user"+sql.toString());
     
     n = sb.insertRecords(sql);
     if(n==1)
     {
    	 if(selplant.equalsIgnoreCase("track"))
         inserted = movdao.insertIntodefaultMovHis(htm);
    	 else
    	 inserted = movdao.insertIntoMovHis(htm);		
     }   
     if(n==1&&inserted==true) {
    	 
    	
    	 response.sendRedirect("../user/summary?RESULT=The User Details has been Updated Sucessfully");
    	 /* result = "<font color=\"green\">The  User ID "+selecteduserid+" has been successfully updated.</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">"; */
     }
     else   { 
    	 response.sendRedirect("../user/summary?RESULT= Could not update User");
    	 /* result = "<font color=\"red\"> Could not update User - "+selecteduserid+" <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">"; */
     }
              df.insertToLog(session, "Updating User ID", result);   //  Inserting into the user log
     

    }
             result = "<h3>"+result;
             session.setAttribute("RESULT",result);
          
%>


<script language="JavaScript">
    document.form1.action = "displayResult2User.jsp";
    document.form1.submit();

</script>


<%@ include file="footer.jsp" %>
</form>
</body>
</html>
