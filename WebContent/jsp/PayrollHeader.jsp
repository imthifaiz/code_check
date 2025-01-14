<%@page import="java.util.Map"%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<jsp:useBean id="miscbean" class="com.track.gates.miscBean" />
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<%@ page import="com.track.gates.*"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <%String pageTitle = request.getParameter("title");
  String mainMenu = request.getParameter("mainmenu");
  String subMenu = request.getParameter("submenu");%>
  <title>SUNPRO <%=pageTitle%></title>
  <%String IMAGEPATH="";
	java.util.ArrayList invQryList  = new java.util.ArrayList();
	java.util.List arr   = new java.util.ArrayList();
	userBean _userBean      = new userBean();
	java.util.Hashtable ht = new java.util.Hashtable();
	String LUSER =session.getAttribute("LOGIN_USER").toString();
	String plant = session.getAttribute("PLANT").toString();
	
	invQryList =_userBean.getUserListforCompany(ht,plant,plant,LUSER);
	arr = new PlantMstUtil().getPlantMstDetails(plant);
	for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		 
		java.util.Map lineArr = (java.util.Map) invQryList.get(iCnt);
	       IMAGEPATH    = (String)lineArr.get("IMAGEPATH");
	}
	String region = session.getAttribute("REGION").toString();
	
	String isInv="0", isAcc="0", isPyrl="0", 
			invClass = "mini-contents-disabled", isAccClass = "mini-contents-disabled",
			isPyrlClass = "mini-contents-disabled";
			if(arr.size()>0){
				for (int i =0; i<arr.size(); i++){
					 Map m = (Map) arr.get(i);
					 isInv = (String)m.get("ENABLE_INVENTORY");
					 isAcc = (String)m.get("ENABLE_ACCOUNTING");
					 isPyrl = (String)m.get("ENABLE_PAYROLL");
				}
			}
			invClass = (isInv.equalsIgnoreCase("1"))? "mini-contents" : invClass;
			isAccClass = (isAcc.equalsIgnoreCase("1"))? "mini-contents" : isAccClass;
			isPyrlClass = (isPyrl.equalsIgnoreCase("1"))? "mini-contents" : isPyrlClass;
	%>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="../jsp/dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="../jsp/dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="../jsp/dist/css/ionicons.min.css">
  <!-- DataTables -->
  <%--<link rel="stylesheet" href="../jsp/dist/css/dataTables.bootstrap.min.css"> 
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs/jszip-2.5.0/dt-1.10.16/b-1.5.1/b-colvis-1.5.1/b-html5-1.5.1/fh-3.1.3/r-2.2.1/rg-1.0.2/sl-1.2.5/datatables.min.css"/>
  --%>
  <link rel="stylesheet" href="../jsp/dist/css/lib/datatables.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="../jsp/dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <!-- <link rel="stylesheet" href="../jsp/dist/css/_all-skins.min.css"> -->

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  <!-- jQuery 3 -->
  <script src="../jsp/dist/js/jquery.min.js"></script>
	<!-- jQuery UI -->
	<%--<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> --%>
	<script src="../jsp/dist/js/jquery-ui-1.12.1.js"></script>
	<%--<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> --%>
	<link rel="stylesheet" href="../jsp/dist/css/jquery-ui-1.12.1.css">
  <!-- Google Font -->
  <%--<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> --%>
  <link rel="stylesheet" href="../jsp/dist/css/google-fonts.css">
  <link rel="stylesheet" href="../jsp/dist/css/select2.min.css">
  <link rel="stylesheet" href="../jsp/dist/css/main-style.css">
  <link rel="stylesheet" href="../jsp/dist/css/bootstrap-toggle.min.css">
<script src="../jsp/dist/js/bootstrap-toggle.min.js"></script>
<script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
  <script>
  $(window).on('load', function() {
	  $("#loader").hide();
});
  function backNavigation(page, tab){
	  window.location.href=page + "?t=" + tab; 
  }
function convertCharToString(strToConvert){
	var str="";
	for(var i=0;i<strToConvert.length;i++){
		if (strToConvert[i] == "~")
	    	str = str.concat("SCTILDESC");
		else if (strToConvert[i] ==("`"))
			str = str.concat("SCBACKTICKSC");
		else if (strToConvert[i] ==("!"))
			str = str.concat("SCEXCLAMATIONSC");
		else if (strToConvert[i] ==("@"))
			str = str.concat("SCATTHERATESC");
		else if (strToConvert[i] ==("#"))
			str = str.concat("SCHASHSC");
		else if (strToConvert[i] ==("$"))
			str = str.concat("SCDOLLARSC");
		else if (strToConvert[i] ==("%"))
			str = str.concat("SCPERCENTAGESC");	
		else if (strToConvert[i] ==("^"))
			str = str.concat("SCCARETSC");
		else if (strToConvert[i] ==("&"))
			str = str.concat("SCAMPRASANDSC");		
		else if (strToConvert[i] ==("*"))
			str = str.concat("SCASTERISKSC");	
		else if (strToConvert[i] ==("("))
			str = str.concat("SCLEFTPARENTHESISSC");		
		else if (strToConvert[i] ==(")"))
			str = str.concat("SCRIGHTPARENTHESISSC");		
		else if (strToConvert[i] ==("_"))
		str = str.concat("SCUNDERSCORESC");		
		else if (strToConvert[i] ==("-"))
			str = str.concat("SCMINUSSC");	
		else if (strToConvert[i] ==("+"))
			str = str.concat("SCPLUSSC");		
		else if (strToConvert[i] ==("="))
			str = str.concat("SCEQUALSIGNSC");	
		else if (strToConvert[i] ==("{"))
			str = str.concat("SCLEFTBRACESC");	
		else if (strToConvert[i] ==("}"))
			str = str.concat("SCRIGHTBRACESC");		
		else if (strToConvert[i] ==("]"))
			str = str.concat("SCRIGHTBRACKETSC");		
		else if (strToConvert[i] ==("\\["))
		str = str.concat("SCLEFTBRACKETSC");		
		else if (strToConvert[i] ==("|"))
			str = str.concat("SCVERTICALBARSC");	
		else if (strToConvert[i] ==("\\"))
			str = str.concat("SCBACKSLASHSC");		
		else if (strToConvert[i] ==(":"))
			str = str.concat("SCKOLONSC");	
		else if (strToConvert[i] ==(";"))
			str = str.concat("SCSEMOCOLONSC");		
		else if (strToConvert[i] ==("\""))
			str = str.concat("SCDOUBLEQOUTSSC");		
		else if (strToConvert[i] ==("\'"))
			str = str.concat("SCSINGLEQOUTSSC");		
		else if (strToConvert[i] ==("<"))
			str = str.concat("SCLESSTHANSC");		
		else if (strToConvert[i] ==(">"))
			str = str.concat("SCGREATERTHANSC");	
		else if (strToConvert[i] ==("."))
			str = str.concat("SCFULLSTOPSC");		
		else if (strToConvert[i] ==("?"))
			str = str.concat("SCQUESTIONMARKSC");	
		else if (strToConvert[i] ==("/"))
			str = str.concat("SCSLASHSC");
		else if (strToConvert[i] ==(","))
			str = str.concat("SCCOMMASC");
		else {
			str = str.concat(strToConvert[i]);
		   }	   
	}
	return str;

	}
	
function addZeroes( num ) {
    var value = Number(num);
    var res = num.split(".");
    if(res.length == 1 || (res[1].length < 3)) {
        value = value.toFixed(2);
    }else if((res[1].length < 4)){
    	value = value.toFixed(3);
    }else if((res[1].length < 5)){
    	value = value.toFixed(4);
    }else if((res[1].length < 6)){
    	value = value.toFixed(5);
    }
    return value;
}
</script>

</head>
<%
String errmsg="Expirydate not found";
boolean bflag=false,errflg=false;String company1="",dateres="",companyname="",notificationCount="";
java.util.Date expiryDate = new java.util.Date();
long twoMonthMillies = 2 * 30 * 24 * 60 * 60 * 1000L;
try{
java.util.HashMap<String, String> loggerDetailsHasMap2 = new java.util.HashMap<String, String>();
loggerDetailsHasMap2.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap2.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger2 = new MLogger();
mLogger2.setLoggerConstans(loggerDetailsHasMap2);
ubean.setmLogger(mLogger2);
	session = request.getSession();
	//Retrieve expiry date
	String expirydatedb = ubean.getExpiryDate(session.getAttribute("PLANT").toString());
	companyname = ubean.getCompanyName(session.getAttribute("PLANT").toString()).toUpperCase();
	//String expirydatedb = session.getAttribute("ACTUALEXPIRY").toString();
	String expirydate2arr[] = expirydatedb.split("-");
	String expirydate2 = StrUtils.arrayToString(expirydate2arr, "");
	String actualexpirydate = DateUtils.addByMonth(expirydatedb,1);
	String expirydate1[] = actualexpirydate.split("-");
	String actexpirydate2 = StrUtils.arrayToString(expirydate1, "");
	 bflag = miscbean.iSExistStartndEND(expirydate2,actexpirydate2);
	String s3 = session.getAttribute("EXPIRYDATE").toString();
	String[] test = null;
	test = s3.split("-");
	StrUtils.reverse(test);
	dateres = StrUtils.arrayToString(test, "-");
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
	expiryDate = sdf.parse(dateres);
	company1 = session.getAttribute("PLANT").toString();
	company1 = company1.toUpperCase();
	notificationCount = new userBean().getNotificationCount(plant, (String) session.getAttribute("LOGIN_USER")).trim();
	if(notificationCount.equalsIgnoreCase("0")){
		notificationCount = "";
	}
}
catch(Exception e)
{
	errflg=true;
	System.out.println("in body exception msg" +e.getMessage());
	errmsg =e.getMessage();
}
%>

<body class="hold-transition skin-blue sidebar-mini">
<div id="loader"></div>
<div class="wrapper">

  <header class="main-header">
    <!-- Logo -->
    <a href="" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><img src="../jsp/dist/img/logo-xs.png" class="logo-xs" onclick="{location.href='../jsp/PayrollHome.jsp';}"></span>
      <!-- logo for regular state and mobile devices -->
      <!-- <span class="logo-lg"><img src="../jsp/dist/img/Final-01.png" onclick="{location.href='../jsp/PayrollHome.jsp';}"></span> -->
      <span class="logo-lg"><img src="../jsp/dist/img/logo-01.png" onclick="{location.href='../jsp/PayrollHome.jsp';}"></span>
       
    </a>
    <a href="#" class="pull-left" data-container="body" data-toggle="popover" style="margin-top: 15px;color:#1d1d1d; font-size:16px; font-weight: 400;">
         <i class="fa fa-angle-down pull-right"></i>
    </a>
    
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      <form name="frmViewMovementHistory" method="post" target="_blank" 
			action="../jsp/view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
			<input type="hidden" name="PGaction" value="View" /> <input
				type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
				name="TO_DATE" value="" /> <input type="hidden" name="USERID"
				value="" />
	  </form>
      <a href="#" role="button" class="fa fa-history fa-2x link" title="See activity logs" data-toggle="tooltip" id="movHisIcon" data-placement="bottom"
      onclick="navigateToMovementHistory()" aria-hidden="true" style="float: left;padding: 15px 10px;font-size: 20px;color: rgba(0,0,0,0.5)"></a>
      
<%if(expiryDate.getTime() - new java.util.Date().getTime() <= twoMonthMillies){ %>      
 <div class="exp-notify bg-green"><p>Expiry Date : <%=dateres%><!-- <a href="">Subscribe Now</a> --></p></div>
 <%} %>
 	  
      <div class="navbar-custom-menu col-xs-10 col-sm-5 no-padding">
        
        <ul class="nav navbar-nav" style="width:100%">
          <!-- Messages: style can be found in dropdown.less-->
        <li style="width:55%" class="menu_bar">
        	<div class="form-group has-feedback" style="margin:15px 15px;">
	        	<span class="glyphicon glyphicon-search form-control-feedback" style="color:#00dc82;"></span>
	        	<input type="text" style="border-radius: 15px;" class="menuSearch form-control" placeholder="Search">
        	</div>
       	</li>
       	<li title="Notification" data-toggle="tooltip" data-placement="bottom" style="width:10%">
       		<a href="../jsp/notification.jsp" role="button" class="fa fa-bell-o"  
       		 style="font-size: 30px;padding: 15px 15px;color: rgba(0,0,0,0.5);">
       		 	<span class="badge"  style="position: absolute;top: 10px;right: 5px;"><%=notificationCount%></span>
       		 </a>
       		 
       	</li>
       	<li title="Help & Support" data-toggle="tooltip" data-placement="bottom" aria-hidden="true" style="width:10%">
       		<a href="https://u-clo.com/support/" target="_blank" role="button" class="fa fa-question-circle-o"  
       		 style="font-size: 30px;padding: 15px 15px;color: rgba(0,0,0,0.5);"></a>
       	</li>
          <li class="dropdown user user-menu" style="width:25%">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">       
              <img class="imgrounded-circle" src="<%=(IMAGEPATH.equalsIgnoreCase("")) ? "../jsp/images/trackNscan/nouser.png" : "/track/ReadFileServlet/?fileLocation="+IMAGEPATH%>">              
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class=""> <a class="#"> <%=session.getAttribute("LOGIN_USER").toString()%> </a> </li>
              <% 
              	PlantMstUtil _PlantMstUtil =new PlantMstUtil();
              boolean isAccountingEnabled = _PlantMstUtil.isAccountingModuleEnabled(session.getAttribute("PLANT").toString());
 				if (isAccountingEnabled){
          request.getSession(false).setAttribute("isAccountingEnabled", "yes");
          %>
            <%-- <li class=""> <a href="<%=request.getContextPath() %>/AccountingBridgeServlet"> Accounting </a> </li> --%>
            <%}else{request.getSession(false).setAttribute("isAccountingEnabled", "no");} %>
              <li class=""> <a href="../jsp/logout.jsp"> Sign Out </a> </li>
              <!-- Menu Body -->
              
              <!-- Menu Footer-->
              
            </ul>
          </li>
          <!-- Control Sidebar Toggle Button -->
          
        </ul>
      </div>
    </nav>
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <ul class="sidebar-menu" data-widget="tree" id="myMenu">
        <li class="<%="Dashboard".equals(pageTitle) ? "active" : ""%>">
          <a href="../jsp/PayrollHome.jsp">
            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
          </a>
        </li>
        <%
			java.util.ArrayList menulist = (java.util.ArrayList)session.getAttribute("DROPDOWN_MENU");
        	java.util.ArrayList menuListWithSequence = (java.util.ArrayList)session.getAttribute("DROPDOWN_MENU_WITH_SEQUENCE");
        	java.util.Hashtable htMenuItems = new java.util.Hashtable<String, String>();
        	java.util.Map htMenuItemDetail = new java.util.HashMap<String, String>();
        	java.util.Iterator iterator = htMenuItemDetail.keySet().iterator();
		%>
        <li class="<%=IConstants.SETTINGS.equals(mainMenu) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-cog"></i> <span>Settings</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>	
          <ul class="treeview-menu" style="display: <%=IConstants.SETTINGS.equals(mainMenu) ? "block" : "none"%>;">
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(0);
          if(htMenuItems.size()>0)
          {
          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.USER_ADMIN.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			User Admin
          		</a>
          		<ul class="dropdown-menu menu-items"><li>
          			<div class="tab-pane tab-25">
          				<div class="row menu-head-row">
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Company</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">User</strong></div></div>
	          				<div class="col-md-5"><div class="col-md-12"><strong class="menu-head">User Access Rights Group</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
	          				<div class="col-md-4">
	          					<%
		          					 htMenuItems = (java.util.Hashtable)menuListWithSequence.get(0); //	First row
		          					 htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
				                	iterator = htMenuItemDetail.keySet().iterator();
				                	while(iterator.hasNext()){
				                		String menuItemTitle = "" + iterator.next();
				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
						         %>
						                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
						               <%		                			
									}
								%>
	          				</div>
	          				<div class="col-md-3">
	          					<%
		          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
				                	iterator = htMenuItemDetail.keySet().iterator();
				                	while(iterator.hasNext()){
				                		String menuItemTitle = "" + iterator.next();
				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
						        %>
						                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
								<%		                			
									}
								%>
	          				</div>
	          				<div class="col-md-5">
	          					<%
	          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
	          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
			                	iterator = htMenuItemDetail.keySet().iterator();
			                	while(iterator.hasNext()){
			                		String menuItemTitle = "" + iterator.next();
			                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
					                %>
					                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
					               <%		                			
								} %>
	          				</div>
          				</div>
		              </div>
          		</li></ul>
          	</li>
          	<% } %>
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(1);
          if(htMenuItems.size()>0)
          {
          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.SYSTEM_MASTER.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			System Master
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-26">
          				<div class="row menu-head-row">
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Product</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Product Class/Type/Brand</strong></div></div>
	          				<div class="col-md-2"><div class="col-md-12"><strong class="menu-head">Location</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Reason Code</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
          		<div class="col-md-4">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(1); //	Second row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-2">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
				</div>                
              </div></li></ul>
          	</li>
          	<% } %>
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(2);
          if(htMenuItems.size()>0)
          {
          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.SYSTEM_ADMIN.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			System Admin
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-25">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Supplier</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Customer</strong></div></div>	          				
	          				<div class="col-md-5"><div class="col-md-12"><strong class="menu-head">Import Data Templates</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">          		
          		<div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(2); //	Third row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-4">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:125%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>          		
          		<div class="col-md-5">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:110%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div></div></div>
              </li></ul>
          	</li>          	
          	 <% } %>
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(3);
          if(htMenuItems.size()>0)
          {
          %>         	
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.ORDER_ADMIN.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Order Admin
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-26">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Order Type / Payment Type</strong></div></div>
	          				<div class="col-md-2"><div class="col-md-12"><strong class="menu-head">VAT / Currency</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12" style="width: 115%;"><strong class="menu-head">HSCODE / COO / Remarks / INCOTERM / Footer</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Kit</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(3); //	Fourth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-2">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-4">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li></ul>
          	</li>
          	<% } %>
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(4);
          if(htMenuItems.size()>0)
          {
          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.PRINTOUT_CONFIGURATION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Printout Configuration
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-5">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Rental / Consignment</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods Receipt / Issue / Stock Move</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(4); //	Fifth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:118%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		<div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="../jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li></ul>
          	</li>
          	<% } %>
          	<%-- <li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Approval/Email Configuration
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-24">
          		          				<div class="row menu-head-row">	          				
	          				<div class="col-md-12" style="width:120%;"><div class="col-md-12"><strong class="menu-head">Order Approval/Email Configuration</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(20); //	Twenty one row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><span class="menu-link" onclick="location.href='../jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
			  </ul>
          	</li> --%>
          	
          </ul>
        </li>
        

          <!-- New Menu Changes (Azees) - 31.7.20--> 
          <li class="<%=IConstants.PAYROLL.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-credit-card"></i> <span>Payroll</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.PAYROLL.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(44);
	          if(htMenuItems.size()>0)
	          {
	          %>
	          <li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(44); //	Forty five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(45); //	Forty six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(51); //	Fity two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.SALARY_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(46); //	Forty seven row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.LEAVE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(52); //	Fity three row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.HOLIDAY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(56); //	Fity Seven row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="<%=menuItemURL%>" class="<%=IConstants.PAYROLL_ADDITION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(55); //	Fity Six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_DEDUCTION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(53); //	Fity FOUR row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_SUMMARY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(54); //	Fity Five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="../jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_REPORT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
	          <%} %>
          </ul>
          </li>

    </section>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
  <section class="content-header">
      <img class="dash-logo" src="<%=request.getContextPath() %>/GetCustomerLogoServlet">
      <span style="margin-left: 15px;font-size: 20px;font-weight: bold;vertical-align: middle;"><%=companyname%></span>
      <%-- <ol class="breadcrumb">
      	<%if ("Home Page".equals(pageTitle)){ %>
        <li class="active"><a href="../home"><i class="fa fa-dashboard"></i> <%=pageTitle%></a></li>
        <%}else if ("Settings".equals(pageTitle)){ %>
        <li class="active"><a href="settings.jsp"><i class="fa fa-cog"></i> <%=pageTitle%></a></li>
        <%}%>
      </ol> --%>
    </section>
<script>
	$(document).ready(function(){
		$('.menu-link').each(function(){
			$(this).parent().css('margin-top', '5px').css('margin-bottom', '5px');
		});
		$("#mySearch").on("keyup", function () {
			if (this.value.length > 0) {   
			  $("li").hide().filter(function () {
			    return $(this).text().toLowerCase().indexOf($("#mySearch").val().toLowerCase()) != -1;
			  }).show(); 
			}  
			else { 
			  $("li").show();
			}
			});
		
		$("#movHisIcon").tooltip();
		
		$(".menuSearch").typeahead({
		  hint: false,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TEXT',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/DashboardServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : '<%=plant%>',
					ACTION : "GET_MENU_LIST_FOR_SUGGESTION",
					TEXT : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.menus);
				}
				});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><a class="menu-link" href="'+data.URL+'">'+data.TEXT+'</a></div>';
				}
			  }
		}).on('typeahead:select',function(event,selection){
			$(this).val("");
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			$(this).val("");
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
		var content = '<div class="col-sm-4 text-center <%=invClass%>">'+
			'<img src="../jsp/dist/img/ordermgt.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Inventory/Order Management</p>'+
		'</div>'+
	'<div class="col-sm-4 text-center <%=isAccClass%>">'+
		'<img src="../jsp/dist/img/accounting.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Accounting</p>'+
	'</div>'+
	'<div class="col-sm-4 text-center <%=isPyrlClass%>">'+
		'<img src="../jsp/dist/img/payroll.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 10px;">Payroll</p>'+
	'</div>';
		
		$('[data-toggle="popover"]').popover({content: content, trigger: "focus", html: true, placement: "bottom"});
	});

	function getFromDateForPeriod(period){
		if (period == 'Last 30 days'){
			return moment().add(-30, 'days');//
		}else if (period == 'Today'){
			return moment();//
		}else if (period == 'Tomorrow'){
			return moment().add(1, 'days');//
		}else if (period == 'This week'){
			return moment().startOf('week');//
		}else if (period == 'This month'){
			return moment().startOf('month');//
		}else if (period == 'This quarter'){
			return moment().startOf('quarter');//
		}else if (period == 'This year'){
			return moment().startOf('year');//
		}else if (period == 'Tomorrow (F)'){
			return moment().add(1, 'days');//
		}else if (period == 'This month (F)'){
			return moment().add(1, 'days');//
		}else if (period == 'This quarter (F)'){
			return moment().add(1, 'days');//
		}else if (period == 'This year (F)'){
			return moment().add(1, 'days');//
		}else if (period == 'Last month'){
			return moment().add(-1, 'month').startOf('month');//
		}else if (period == 'Last quarter'){
			return moment().add(-1, 'quarter').startOf('quarter');//
		}else if (period == 'Last year'){
			return moment().add(-1, 'year').startOf('year');//
		}else if (period == 'Next 30 days'){
			return moment().add(1, 'days');//
		}
	}

	function getToDateForPeriod(period){
		if (period == 'Last 30 days'){
			return moment().add(-1, 'days');//
		}else if (period == 'Today'){
			return moment();//
		}else if (period == 'Tomorrow'){
			return moment().add(1, 'days');//
		}else if (period == 'This week'){
			return moment().endOf('week');//
		}else if (period == 'This month'){
			return moment().endOf('month');//
		}else if (period == 'This quarter'){
			return moment().endOf('quarter');//
		}else if (period == 'This year'){
			return moment().endOf('year');//
		}else if (period == 'Tomorrow (F)'){
			return moment().add(1, 'days');//
		}else if (period == 'This month (F)'){
			return moment().endOf('month');//
		}else if (period == 'This quarter (F)'){
			return moment().endOf('quarter');//
		}else if (period == 'This year (F)'){
			return moment().endOf('year');//
		}else if (period == 'Last month'){
			return moment().add(-1, 'month').endOf('month');//
		}else if (period == 'Last quarter'){
			return moment().add(-1, 'quarter').endOf('quarter');//
		}else if (period == 'Last year'){
			return moment().add(-1, 'year').endOf('year');//
		}else if (period == 'Next 30 days'){
			return moment().add(30, 'days');//
		}
	}
	
	function navigateToMovementHistory(){
		document.forms['frmViewMovementHistory'].FROM_DATE.value=getFormattedDate(moment(), 'DD/MM/YYYY');
		document.forms['frmViewMovementHistory'].USERID.value='<%=((String) session.getAttribute("LOGIN_USER")).trim()%>';
		document.forms['frmViewMovementHistory'].submit();
	}	
</script>