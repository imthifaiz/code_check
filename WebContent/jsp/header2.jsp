<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@page import="com.track.dao.DoHdrDAO"%>
<%@page import="com.track.dao.ParentChildCmpDetDAO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.*"%>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.db.util.*"%>
<%@page import="com.track.gates.*"%>
<jsp:useBean id="miscbean" class="com.track.gates.miscBean" />
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<%! @SuppressWarnings({"rawtypes"}) %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <%String pageTitle = request.getParameter("title");
  String mainMenu = request.getParameter("mainmenu");
  String subMenu = request.getParameter("submenu");
  boolean isInternalRequest = !"".equals(StrUtils.fString(request.getParameter("INTERNAL_REQUESET")));
  String rootURI = HttpUtils.getRootURI(request);
  %>
  <title>SUNPRO <%=pageTitle%></title>
  <%String IMAGEPATH="";
	java.util.ArrayList invQryList  = new java.util.ArrayList();
	java.util.List arr   = new java.util.ArrayList();
	userBean _userBean      = new userBean();
	java.util.Hashtable ht = new java.util.Hashtable();
	String LUSER =StrUtils.fString((String)session.getAttribute("LOGIN_USER"));
	String pwd =StrUtils.fString((String)session.getAttribute("IDS"));
	if("".equals(LUSER) && isInternalRequest)
	{
		LUSER= StrUtils.fString(request.getParameter("LOGIN_USER"));
	}
	
	String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
	if("".equals(plant) && isInternalRequest)
	{
		plant= StrUtils.fString(request.getParameter("PLANT"));
	}
	String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENTBYCHILD(plant);
	if(PARENT_PLANT==null){
		PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);
	if(PARENT_PLANT==null)
		PARENT_PLANT="";
	}
	String systatus = StrUtils.fString((String)session.getAttribute("SYSTEMNOW"));
	if("".equals(systatus) && isInternalRequest)
	{
		systatus= StrUtils.fString(request.getParameter("SYSTEMNOW"));
	}
	String ISPEPPOL =new PlantMstDAO().getisPeppol(plant);
	String  ENABLE_POS =new PlantMstDAO().getispos(plant);
	String  MANAGEWORKFLOW =new PlantMstDAO().getMANAGEWORKFLOW1(plant);
	invQryList =_userBean.getUserListforCompany(ht,plant,plant,LUSER);
	arr = new PlantMstUtil().getPlantMstDetails(plant);
	for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		 
		java.util.Map lineArr = (java.util.Map) invQryList.get(iCnt);
	    IMAGEPATH    = (String)lineArr.get("IMAGEPATH");
	    if (!new java.io.File(IMAGEPATH).exists()){
	    	IMAGEPATH = "";
	    }
	}
	String region = StrUtils.fString((String)session.getAttribute("REGION"));
	if("".equals(region) && isInternalRequest)
	{
		region= StrUtils.fString(request.getParameter("REGION"));
	}
	String contextPath = request.getContextPath();
	
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
			/* invClass = (isInv.equalsIgnoreCase("1"))? "mini-contents" : invClass;
			isAccClass = (isAcc.equalsIgnoreCase("1"))? "mini-contents" : isAccClass;
			isPyrlClass = (isPyrl.equalsIgnoreCase("1"))? "mini-contents" : isPyrlClass; */
			
			invClass = (isInv.equalsIgnoreCase("1"))? "inventrycontents" : invClass;
			isAccClass = (isAcc.equalsIgnoreCase("1"))? "accountcontents" : isAccClass;
			isPyrlClass = (isPyrl.equalsIgnoreCase("1"))? "payrollcontents" : isPyrlClass;
			
			String AutoPopupCounter="1",AutoPopupDelay="",AutoPopupOrderType="",enableOutletAutoPrintPopup="0";
			 Map m =  new HashMap();		     
		       m=new DoHdrDAO().getDOReciptHeaderDetails(plant,"Outbound order");
		       if(!m.isEmpty()){
		    	  // AutoPopupDelay =StrUtils.fString((String)session.getAttribute("AUTOPOPUPDELAY"));
		    	  // if("".equals(AutoPopupDelay) && "null".equals(AutoPopupDelay))
		    		//{
		    	   AutoPopupDelay = (String) m.get("AUTOPOPUPDELAY");
		    	   AutoPopupOrderType = (String) m.get("AUTOPOPUPORDERTYPE");
		    	  //	session.setAttribute("AUTOPOPUPDELAY", AutoPopupDelay);
		    		//}
		           enableOutletAutoPrintPopup = (String)m.get("ENABLEOUTLETAUTOPRINTPOPUP");
		       }
	%>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/bootstrap.min.css"/>
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/font-awesome.min.css"/>
  <!-- Ionicons -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/ionicons.min.css"/>
  <!-- DataTables -->
  <%--<link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/dataTables.bootstrap.min.css"> 
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs/jszip-2.5.0/dt-1.10.16/b-1.5.1/b-colvis-1.5.1/b-html5-1.5.1/fh-3.1.3/r-2.2.1/rg-1.0.2/sl-1.2.5/datatables.min.css"/>
  --%>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/lib/datatables.min.css"/>
  <!-- Theme style -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/AdminLTE.min.css"/>
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <!-- <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/_all-skins.min.css"> -->

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  <!-- jQuery 3 -->
  <script src="<%=rootURI%>/jsp/dist/js/jquery.min.js"></script>
	<!-- jQuery UI -->
	<%--<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> --%>
	<script src="<%=rootURI%>/jsp/dist/js/jquery-ui-1.12.1.js"></script>
	<%--<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> --%>
	<link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/jquery-ui-1.12.1.css">
  <!-- Google Font -->
  <%--<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> --%>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/google-fonts.css"/>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/select2.min.css"/>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/main-style.css"/>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/bootstrap-toggle.min.css"/>
  <style type="text/css">
  	.dataTable tr th{
  		text-align:center !important;
  	}
  	
  	.table-responsives {
    width: 100%;
    margin-bottom: 15px;
    overflow-y: hidden;
    }
    .navbar-custom-menu>.navbar-nav>li>.dropdown-menu {
    position: absolute;
    right: 0;
    left: auto;
    top: 45px;
}
    
  </style>
<script src="<%=rootURI%>/jsp/dist/js/bootstrap-toggle.min.js"></script>
<script src="<%=rootURI%>/jsp/dist/js/moment.min.js"></script>
<script src="<%=rootURI%>/jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css"/>
  <script>
  $(window).on('load', function() {
	  $("#loader").hide();
});
  function backNavigation(page, tab){
	  window.location.href=page + "?t=" + tab; 
  }
  
  $(document).ready(function(){
  $(".inventrycontents").click(function(){
		//window.location.href='<%=rootURI%>/home';
		window.location.href =  "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=INVENTORY";
	});

	$(".accountcontents").click(function(){
		//window.location.href='<%=rootURI%>/home';
		window.location.href =  "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=ACCOUNTING";
	});

	$(".payrollcontents").click(function(){
		//window.location.href='PayrollHome.jsp';
		window.location.href =  "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=PAYROLL";
	});
  });
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
loggerDetailsHasMap2.put(MLogger.COMPANY_CODE, plant);
loggerDetailsHasMap2.put(MLogger.USER_CODE, LUSER);
MLogger mLogger2 = new MLogger();
mLogger2.setLoggerConstans(loggerDetailsHasMap2);
ubean.setmLogger(mLogger2);
	session = request.getSession();
	//Retrieve expiry date
	String expirydatedb = ubean.getExpiryDate(plant);
	companyname = ubean.getCompanyName(plant).toUpperCase();
	/*String expirydatedb = session.getAttribute("ACTUALEXPIRY").toString();
	if("".equals(expirydatedb) && isInternalRequest)
	{
		expirydatedb= StrUtils.fString(request.getParameter("ACTUALEXPIRY"));
	}*/
	String expirydate2arr[] = expirydatedb.split("-");
	String expirydate2 = StrUtils.arrayToString(expirydate2arr, "");
	String actualexpirydate = DateUtils.addByMonth(expirydatedb,1);
	String expirydate1[] = actualexpirydate.split("-");
	String actexpirydate2 = StrUtils.arrayToString(expirydate1, "");
	 bflag = miscbean.iSExistStartndEND(expirydate2,actexpirydate2);
	String s3 = StrUtils.fString((String)session.getAttribute("EXPIRYDATE"));
	if("".equals(s3) && isInternalRequest)
	{
		s3= StrUtils.fString(request.getParameter("EXPIRYDATE"));
	}
	String[] test = null;
	test = s3.split("-");
	StrUtils.reverse(test);
	dateres = StrUtils.arrayToString(test, "-");
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
	expiryDate = sdf.parse(dateres);
	company1 = plant.toUpperCase();
	notificationCount = new userBean().getNotificationCount(plant, LUSER).trim();

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
<%if (!isInternalRequest){ %>
<div id="loader"></div>
<%} %>
<div class="wrapper">
  <%if (!isInternalRequest){ %>

  <header class="main-header">
    <!-- Logo -->
    <a href="" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><img src="<%=rootURI%>/jsp/dist/img/logo-xs.png" class="logo-xs" onclick="{location.href='<%=rootURI%>/home';}"></span>
      <!-- logo for regular state and mobile devices -->
      <%-- <span class="logo-lg"><img src="<%=rootURI%>/jsp/dist/img/Final-01.png" onclick="{location.href='<%=rootURI%>/home';}"></span> --%>
      <span class="logo-lg"><img src="<%=rootURI%>/jsp/dist/img/logo-01.png" onclick="{location.href='<%=rootURI%>/home';}"></span>
       
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
			action="<%=rootURI%>/jsp/view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
			<input type="hidden" name="PGaction" value="View" /> <input
				type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
				name="TO_DATE" value="" /> <input type="hidden" name="USERID"
				value="" />
				<input type="hidden" name="AUTOPOPUPCOUNTER" id="AUTOPOPUPCOUNTER"
				value="<%=AutoPopupCounter%>" />
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
       		<a href="<%=rootURI%>/notification" role="button" class="fa fa-bell-o"
       		 style="font-size: 30px;padding: 15px 15px;color: rgba(0,0,0,0.5);">
       		 	<span class="badge"  style="position: absolute;top: 10px;right: 5px;"><%=notificationCount%></span>
       		 </a>
       		 
       	</li>
       	<li title="Help & Support" data-toggle="tooltip" data-placement="bottom" aria-hidden="true" style="width:10%">
       		<a href="https://sunpro.com.sg/support/" target="_blank" role="button" class="fa fa-question-circle-o"  
       		 style="font-size: 30px;padding: 15px 15px;color: rgba(0,0,0,0.5);"></a>
       	</li>
          <li class="dropdown user user-menu" style="width:10%;height:5%;">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">       
              <img class="imgrounded-circle" src="<%=rootURI + (IMAGEPATH.equalsIgnoreCase("") ? "/jsp/images/trackNscan/nouser.png" : "/ReadFileServlet/?fileLocation="+IMAGEPATH)%>">              
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class=""> <a class="#"> <%=LUSER%> </a> </li>
              <% 
              //int companyCount = new PlantMstDAO().getCompanyCount(LUSER);
              ArrayList arrchk  = new ArrayList();//select multi company fix-azees 21.02.22
              String encrPwd   = eb.encrypt(pwd);
              arrchk= new PlantMstUtil().validateUser(LUSER, encrPwd);
              int companyCount=arrchk.size();
              if (companyCount > 1){
              %>
              <li class=""> <a href="<%=rootURI%>/selectcompany"> Switch Company </a> </li>
              <% 
              }
              	PlantMstUtil _PlantMstUtil =new PlantMstUtil();
              boolean isAccountingEnabled = _PlantMstUtil.isAccountingModuleEnabled(plant);
 				if (isAccountingEnabled){
          request.getSession(false).setAttribute("isAccountingEnabled", "yes");
          %>
            <%-- <li class=""> <a href="<%=request.getContextPath() %>/AccountingBridgeServlet"> Accounting </a> </li> --%>
            <%}else{request.getSession(false).setAttribute("isAccountingEnabled", "no");} %>
              <li class=""> <a href="<%=rootURI%>/jsp/logout.jsp"> Sign Out </a> </li>
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
          <a href="<%=rootURI%>/home">
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
		
		<%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING") || systatus.equalsIgnoreCase("PAYROLL")){ %>
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
						         if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                		Boolean isworkflow=false;
				                		if(menuItemTitle.equals("Manage Workflow")) {
				                		if(MANAGEWORKFLOW.equals("1")){
				                			isworkflow=true;
				                		}
				                		} else {
				                			isworkflow=true;
				                		}
				                if(isworkflow){		
						        if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
									}
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
					                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-5">
          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Product/Promotion</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12" style="width: 152%;"><strong class="menu-head">Product Department/Category/Sub Category/Brand</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Location</strong></div></div>
	          				<div class="col-md-2"><div class="col-md-12"><strong class="menu-head">Reason Code</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
          		<div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(1); //	Second row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}
							} %>
          		</div>
          		<div class="col-md-2">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-5">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Supplier</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Customer</strong></div></div>	          				
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Employee</strong></div></div>	          				
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Import Data Templates</strong></div></div>
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:125%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:125%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>          		
              <div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	 column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:125%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:125%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:110%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:110%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-5">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Order Type / Payment Type / Bill Of Materials</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">GST / VAT / Currency</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12" style="width: 115%;"><strong class="menu-head">HSCODE / COO / Remarks / INCOTERM / Footer</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Distribution</strong></div></div>
	          				<!-- <div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Kit</strong></div></div> -->
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
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		} } %>				                
              </div>
              <div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Consignment</strong></div></div>
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
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}
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
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li></ul>
          	</li>
          	<% } %>
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(70);
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
			<!-- Department -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(61); //	Sixty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.DEPARTMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.DEPARTMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SALARY_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.SALARY_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.LEAVE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.LEAVE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.HOLIDAY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.HOLIDAY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
			<!-- Bank -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(25); //	Twenty Six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<!-- Shift -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(63); //	Sixty four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SHIFT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li>
              <li class="dropdown">
          		<a href="#" class="<%=IConstants.PRINTOUT_CONFIGURATION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Printout Configuration
          		</a>    
          <ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-7">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Bulk Payslip</strong></div></div>	          				
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(70); //	Seventy one row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}
							} %>
              </div>
              </div>
              </div>
          	</li>
          </ul></li>
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
				                  <div class="col-md-12" style="width:120%;"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
			  </ul>
          	</li> --%>
          	
          </ul>
        </li>
        <%}%>
        
        
        
          <% if(!PARENT_PLANT.equalsIgnoreCase("")){%>
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--New Menu (Azees 17.1.21) -->
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(1);
          if(htMenuItems.size()>0)
          {
          %>
          <li class="dropdown">
                  <a href="<%=rootURI%>/product/summary">
                  <i class="fa fa-steam"></i> Product
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
                  </li>
          
          <%} %>
          <%} %>
        <%}%>
          
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--New Menu (Azees 17.1.21) -->
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(62);
          if(htMenuItems.size()>0)
          {
          %>
        <li class="<%=IConstants.PROJECT.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-tasks"></i> <span>Project</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.PROJECT.equals(mainMenu) ? "block" : "none"%>;">
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(62); //	Sixty three row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PROJECT.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		                			
							} %>
          		</li>
          	</ul>
        </li>
        <%} %>
        <%}%>
        <!-- Purchase Estimate -->
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>

      		<li class="<%=IConstants.PURCHASEESTIMATE.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-truck"></i> <span>Purchase Estimate</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
           <ul class="treeview-menu" style="display: <%=IConstants.PURCHASEESTIMATE.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(68);
          if(htMenuItems.size()>0)
          {
              %>
              	<li class="dropdown">
              		<a href="#" class="<%=IConstants.PURCHASEESTIMATE.equals(subMenu) ? "active" : ""%>">
              			<i class="fa fa-angle-right pull-right"></i>
              			Purchase Estimate
              		</a>
              		<ul class="dropdown-menu menu-items"><li>
              			<div class="tab-pane tab-7">
              				<div class="row menu-head-row">
    	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Purchase Estimate</strong></div></div>
              				</div>
              				<div class="row">
              				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
              				</div>
              				<div class="row menu-link-row">
    	          				<div class="col-md-12">
    	          					<%
    		          					htMenuItems = (java.util.Hashtable)menuListWithSequence.get(68);//	Sixty ninth row
    		          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
    		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
    				                	iterator = htMenuItemDetail.keySet().iterator();
    				                	while(iterator.hasNext()){
    				                		String menuItemTitle = "" + iterator.next();
    				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
    						         if (!menuItemURL.contains(".jsp")){
    	                			%>
    				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
    				               <%	
    		                		}else{
    				                %>
    				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
    				               <%		    
    		                		}		                			
    									}
    								%>
    	          				</div>
              				</div>
    		              </div>
              		</li></ul>
              	</li>
              	<%} %>
              	</ul>
              	</li>
              	<%} %>
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--1 New Menu Changes (Azees 18.8.19) -->
        <li class="<%=IConstants.PURCHASE.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-shopping-bag"></i> <span>Purchase</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.PURCHASE.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(5);
          if(htMenuItems.size()>0)
          {
          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.PURCHASE_ORDER.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Purchase Order
          		</a>
          		<ul class="dropdown-menu menu-items"><li>
          			<div class="tab-pane tab-7">
          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Purchase Order</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
	          				<div class="col-md-12">
	          					<%
		          					htMenuItems = (java.util.Hashtable)menuListWithSequence.get(5);//	Sixth row
		          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
				                	iterator = htMenuItemDetail.keySet().iterator();
				                	while(iterator.hasNext()){
				                		String menuItemTitle = "" + iterator.next();
				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
						         if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
									}
								%>
	          				</div>
          				</div>
		              </div>
          		</li></ul>
          	</li>
          	<%} %>
          	 <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(11);
	         // if(htMenuItems.size()>0)
	          //{
	          %>
	          <!-- Purchase Transaction  -->
          		<li class="dropdown"><!--  Author: Azees  Create date: July 30,2021  Description:  Purchase Transaction Dashboard-->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(11); //	Twelth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(5);//	Fivth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_TRANSACTION.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_TRANSACTION.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
	           <%-- <li class="dropdown">
          <a href="<%=rootURI%>/purchaseTransactionDashboard">
            <i class="fa fa-angle-right pull-right"></i> <span>Purchase Transaction</span>
          </a>
        </li> --%>
      <%--     	<li class="dropdown">
          		<a href="#" class="<%=IConstants.PURCHASE_TRANSACTION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Purchase Transaction
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-25">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Purchase</strong></div></div>
	          				<!-- <div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase Reversal</strong></div></div> -->
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Goods Receipt</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Edit Inventory Expiry Date</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-4">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(11); //	Twelth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		<div class="col-md-4">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
        </ul>
          		</li> --%>
        <%//} %>
        <!-- Expenses  -->
          		<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(23); //	Twenty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.EXPENSES.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.EXPENSES.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
          		<!-- Receipt  -->
          		<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(27); //	Twenty Sixth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.RECEIPT.equals(subMenu) ? "active" : ""%>">
				                	Goods Receipt
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.RECEIPT.equals(subMenu) ? "active" : ""%>">
				                	Goods Receipt
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
          		<%-- <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(27);
		          if(htMenuItems.size()>0)
		          {
		          %>
		          	<li class="dropdown">
		          		<a href="#">
		          			<i class="fa fa-angle-right pull-right"></i>
		          			Receipt
		          		</a>
		          		<ul class="dropdown-menu menu-items"><li>
		          			<div class="tab-pane tab-7">
		          				<div class="row menu-head-row">
			          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Goods Receipt</strong></div></div>
		          				</div>
		          				<div class="row">
		          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
		          				</div>
		          				<div class="row menu-link-row">
			          				<div class="col-md-12">
			          					<%
				          					htMenuItems = (java.util.Hashtable)menuListWithSequence.get(27);//	Sixth row
				          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
				          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
						                	iterator = htMenuItemDetail.keySet().iterator();
						                	while(iterator.hasNext()){
						                		String menuItemTitle = "" + iterator.next();
						                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								         %>
								                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
								               <%		                			
											}
										%>
			          				</div>
		          				</div>
				              </div>
		          		</li></ul>
		          	</li>
		          	<%} %> --%>
          		
          		<li class="dropdown">
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(21); //	Twenty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.BILL.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.BILL.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		
          		</li>
          		
          		<li class="dropdown"><!-- Purchase Return  -->
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(30); //	Thirty one row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_RETURN.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_RETURN.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
          		<li class="dropdown"><!-- Product Return  -->
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(75); //	Sevety Six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PRODUCT_RETURN.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PRODUCT_RETURN.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
          		<li class="dropdown"><!-- Purchase Credit Notes  -->
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(26); //	Twenty four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_CREDIT_NOTES.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PURCHASE_CREDIT_NOTES.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
          		<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(16);
          		if(htMenuItems.size()>0)
          		{
          		%>
          		<li class="dropdown">
          		<a href="#" class="<%=IConstants.PURCHASE_REPORTS.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Purchase Reports
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-12">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase With Cost</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Generate PDF</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods / Order Receipt</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(16); //	Seventeenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" ><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:110%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:110%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          <%} %>
          </ul>
          		</li>
        <%}%>
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--2 New Menu Changes (Azees 18.8.19) -->
                <li class="<%=IConstants.ESTIMATE_ORDER.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-calculator"></i> <span>Sales Estimate</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.ESTIMATE_ORDER.equals(mainMenu) ? "block" : "none"%>;">
          <li class="dropdown">
          		<a href="#" class="<%=IConstants.ESTIMATE_ORDER.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales Estimate
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-7">
          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Estimate Order</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
          		<div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(6); //	Seventh row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		} } %>
          		</div>
				</div>                
              </div></li></ul>
          	</li>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.ESTIMATE_REPORTS.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Estimate Reports
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-18">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Estimate</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(17); //	Eighteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
              </div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          </ul>
          </li>
        <%}%>
        <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--3 New Menu Changes (Azees 18.8.19) -->
                <li class="<%=IConstants.SALES.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-line-chart"></i> <span>Sales</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.SALES.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(7);
	          if(htMenuItems.size()>0)
	          {
	          %>
          <li class="dropdown">
          		<a href="#" class="<%=IConstants.SALES_ORDER.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales Order
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-20">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Generate PDF</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">          		
          		<div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(7); //	Eighth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
			  </div></div>
              </li></ul>
          	</li>
          	<%} %>
       <!-- Sales Transaction  -->
          		<li class="dropdown"><!--  Author: Azees  Create date: July 30,2021  Description:  Sales Transaction Dashboard-->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(13); //	Fourteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(5);//	Fivth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SALES_TRANSACTION.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.SALES_TRANSACTION.equals(subMenu) ? "active" : ""%>">
				                	<%=menuItemTitle %>
          						  	<i class="fa fa-angle-right pull-right"></i>
				                </a>
				               <%		    
		                		}		                			
							} %>
          		</li>
	          	           <%-- <li class="dropdown">
          <a href="<%=rootURI%>/salesTransactionDashboard">
            <i class="fa fa-angle-right pull-right"></i> <span>Sales Transaction</span>
          </a> --%>
        </li>
        <%--   	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(13);
	          if(htMenuItems.size()>0)
	          {
	          %>  
        <li class="dropdown">
          		<a href="#" class="<%=IConstants.SALES_TRANSACTION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales Transaction
          		</a>
          <ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-14">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales Return</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods Issue</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12" style="width: 105%;"><strong class="menu-head">Packing List/Deliver Note (PL/DN)</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(13); //	Fourteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %>
          		<!-- Issue  --> --%>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(34); //	thirty three row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.GOODS_ISSUED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							Goods Issued
          						</a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.GOODS_ISSUED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							Goods Issued
          						</a>
				               <%		    
		                		}		                			
							} %>
          	</li>
          		<%-- <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(34);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Issue
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-7">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Goods Issue</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(34); //	thirty Four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>              
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %> --%>
          	<!-- Invoice  -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(24); //	Twenty five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.INVOICE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.INVOICE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		    
		                		}		                			
							} %>
          	</li>	
          	
          	<li class="dropdown"><!-- Sales Return -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(31); //	Thirty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SALES_RETURN.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.SALES_RETURN.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		    
		                		}		                			
							} %>
          	</li>
          	<li class="dropdown"><!-- Product Return Receive-->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(76); //	Seventy Seven row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PRODUCT_RECEIVE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PRODUCT_RECEIVE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		    
		                		}		                			
							} %>
          	</li>
          	<li class="dropdown"><!-- Sales Credit Notes -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(29); //	Thirty row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.CREDIT_NOTES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.CREDIT_NOTES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		    
		                		}		                			
							} %>
          	</li> 
          	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(18);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.SALES_REPORTS.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales Reports
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-19">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales With Price / Average Cost</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Generate PDF</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods / Order Issue</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(18); //	Nineteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %>
          </ul>
          </li>
          <%}%>
          
          <!-- Consignment -->
          <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <!--New Menu (Azees 11.2.21) -->
        <li class="<%=IConstants.CONSIGNMENT.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-truck"></i> <span>Consignment</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          
          <ul class="treeview-menu" style="display: <%=IConstants.CONSIGNMENT.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(64);
          if(htMenuItems.size()>0)
          {
          %>
          		<li class="dropdown">
          		<a href="#" class="<%=IConstants.CONSIGNMENT.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Consignment
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-7">
          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Consignment Order</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
          		<div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(64); //	Sixty five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} } %>
          		</div>
				</div>                
              </div></li></ul>
          	</li>
          	<%} %>
          	
          	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(9);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.CONSIGNMENT_TRANSACTION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Consignment Transaction 
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-18">
          		          				<div class="row menu-head-row">	          				
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Consignment</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(9); //	Tenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
              </div>              
          		</div>
              </div></li></ul>
          	</li>
          	<%} %>
          	
          	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(19);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.CONSIGNMENT_REPORTS.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			 Consignment Reports
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-20">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Consignment</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Generate Consignment PDF</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(19); //	Twentieth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %>
          	
          	</ul>
        </li>
        <%}%>
          
		 <%if(systatus.equalsIgnoreCase("ACCOUNTING")){ %>
          <!-- New Menu Changes (Anandh) - Changed on 11.6.20 (Azees)--> 
          <li class="<%=IConstants.ACCOUNTING.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-money"></i> <span>Accounting</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.ACCOUNTING.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(25);
	          if(htMenuItems.size()>0)
	          {
	          %>
	         <li class="dropdown">
	         <!-- Banking -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(25); //	Twenty six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<!-- Chart of Accounts -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(35); //	Thirty six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.CHART_OF_ACCOUNTS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.CHART_OF_ACCOUNTS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<!-- AP Document -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(73); //	Seventy Four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.APEXPENSES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.APEXPENSES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<!-- POS Expenses -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(74); //	Sventy Five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.POSEXPENSES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.POSEXPENSES.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<li class="dropdown"><!-- Payment Made -->
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(22); //	Twenty Three row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          		</li>
          		<li class="dropdown"><!-- PDC Payment Made -->
          		
          			<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(42); //	Fourty three row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PDC_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PDC_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          		</li>
          	<li class="dropdown"><!-- Payment Received -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(28); //	Twenty nine row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYMENT_RECEIVED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYMENT_RECEIVED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<li class="dropdown"><!-- PDC Payment Receive -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(43); //	Fourty Four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PDC_PAYMENT_RECEIVED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PDC_PAYMENT_RECEIVED.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(36); //	Thirty seven row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.JOURNAL_ENTRY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.JOURNAL_ENTRY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	<li class="dropdown">
          	<!-- Contra Entry -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(37); //	Thirty Eight row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.CONTRA_ENTRY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%	
		                		}else{
				                %>
				                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.CONTRA_ENTRY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%
								}							   
							} %>
          	</li>
          	
          	
          	<%} %>
          </ul>
          </li>
           <%}%>
           
           <%if(systatus.equalsIgnoreCase("ACCOUNTING")){ %>
          <!-- New Menu Changes (Azees) - 12.10.20--> 
          <li class="<%=IConstants.TAX.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-calendar"></i> <span>Tax</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.TAX.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(38);
	          if(htMenuItems.size()>0)
	          {
	          %>
	          <li class="dropdown">
	          <!-- Tax Settings -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(38); //	Thirty Nine row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		System.out.print("Region "+region);
  								if(region.equalsIgnoreCase("GCC"))
  									menuItemURL="tax/"+menuItemURL;
  								else if(region.equalsIgnoreCase("ASIA PACIFIC"))
  									menuItemURL="tax/sg-"+menuItemURL;
				                %>
				                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.TAX_SETTINGS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
							
							<!--  <a href="<%=rootURI%>/jsp/sg-taxreport.jsp">
							 	<i class="fa fa-angle-right pull-right"></i>
          							Tax Reports
          					</a>	 -->
          	</li>
	          <li class="dropdown">
          		<!-- Tax Return -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(41); //	Fourty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		//Tax Region based Tax Summary
      							if(menuItemTitle.equalsIgnoreCase("Tax Return"))
      							{
      								System.out.print("Region "+region);
      								if(region.equalsIgnoreCase("GCC"))
      									menuItemURL="tax/uae-"+menuItemURL;
      								else if(region.equalsIgnoreCase("ASIA PACIFIC"))
      									menuItemURL="tax/sg-"+menuItemURL;
      							}
				                %>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.TAX_RETURN.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		                			
							} %>
              
          	</li>           
          	<li class="dropdown">
          		<!-- Tax Payments -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(39); //	Fourty row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		//Tax Region based Tax Summary
      							if(menuItemTitle.equalsIgnoreCase("Tax Payments"))
      							{
      								System.out.print("Region "+region);
      								if(region.equalsIgnoreCase("GCC"))
      									menuItemURL="uae-"+menuItemURL;
      								else if(region.equalsIgnoreCase("ASIA PACIFIC"))
      									menuItemURL="sg-"+menuItemURL;
      							}
				                %>
				                  <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.TAX_PAYMENTS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		                			
							} %>
              
          	</li>
          	<li class="dropdown">
          		<!-- Tax Adjustments -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(40); //	Fourty One row
          		htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
            	iterator = htMenuItemDetail.keySet().iterator();
            	while(iterator.hasNext()){
            		String menuItemTitle = "" + iterator.next();
            		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
            		if(region.equalsIgnoreCase("GCC"))
							menuItemURL=""+menuItemURL;
						else if(region.equalsIgnoreCase("ASIA PACIFIC"))
							menuItemURL="sg-"+menuItemURL;
	                %>
	                <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.TAX_ADJUSTMENTS.equals(subMenu) ? "active" : ""%>">
							<i class="fa fa-angle-right pull-right"></i>
							<%=menuItemTitle %>
						</a>	
	               <%		                			
				} %>
              
          	</li>
          <%} %>
          
          	<li class="dropdown">
          		<!-- Tax Reports -->
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(65); //	Sixty Six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		//Tax Region based Tax Summary
      							
      								System.out.print("Region "+region);
      								if(region.equalsIgnoreCase("GCC"))
      									menuItemURL="tax/uae-"+menuItemURL;
      								else if(region.equalsIgnoreCase("ASIA PACIFIC"))
      									menuItemURL="tax/sg-"+menuItemURL;
      							
				                %>
				                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.TAX_REPORTS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
				               <%		                			
							} %>
              
          	</li>
          </ul>
          </li>
           <%}%>
           
		 <%if(systatus.equalsIgnoreCase("PAYROLL")){ %>
          <!-- New Menu Changes (Azees) - 31.7.20--> 
          <li class="<%=IConstants.PAYROLL.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-credit-card"></i> <span>Payroll</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.PAYROLL.equals(mainMenu) ? "block" : "none"%>;">
          <%-- <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(44);
	          if(htMenuItems.size()>0)
	          {
	          %> --%>
	          <%-- <li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(44); //	Forty five row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.EMPLOYEE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
			<!-- Department -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(61); //	Sixty two row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.DEPARTMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.DEPARTMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SALARY_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.SALARY_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.LEAVE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.LEAVE_TYPE.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.HOLIDAY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.HOLIDAY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
			<!-- Bank -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(25); //	Twenty Six row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.BANKING.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<!-- Shift -->
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(63); //	Sixty four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.SHIFT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          	</li> --%>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(56); //	Fity Seven row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_ADDITION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_ADDITION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_DEDUCTION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_DEDUCTION.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                   <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_SUMMARY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	 <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_SUMMARY.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(69); //	Seventy row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                   <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYSLIP.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	 <a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYSLIP.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(58); //	Fity Nine row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(59); //	sixty row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.CLAIM.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.CLAIM.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(60); //	sixty one row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.CLAIM_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.CLAIM_PAYMENT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}	                			
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
		                		if (!menuItemURL.contains(".jsp")){
		                			%>
					                  <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_REPORT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%	
			                		}else{
					                %>
					                	<a href="<%=rootURI%>/jsp/<%=menuItemURL%>" class="<%=IConstants.PAYROLL_REPORT.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>
					               <%
									}		                			
							} %>
          	</li>
	          <%-- <%} %> --%>
          </ul>
          </li>          
           <%}%>
		 <%if(systatus.equalsIgnoreCase("BLOCKED")){ %>
          <!--4 New Menu Changes (Azees 19.8.19) -->
                <li class="<%=IConstants.RENTAL_CONSIGNMENT.equals(mainMenu) ? "active" : ""%> treeview">
                  <a href="#">
            <i class="fa fa-university"></i> <span>Rental / Consignment</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.RENTAL_CONSIGNMENT.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(8);
	          if(htMenuItems.size()>0)
	          {
	          %>
          <li class="dropdown">
          		<a href="#" class="<%=IConstants.RENTAL_CONSIGNMENT_ORDER.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Rental / Consignment Order 
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-20">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Rental</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Consignment</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(8); //	Ninth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li></ul>
          	</li>
          	<%} %>
          	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(9);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.RENTAL_CONSIGNMENT_TRANSACTION.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Rental / Consignment Transaction 
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-20">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Rental</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Consignment</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(9); //	Tenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li></ul>
          	</li>
          	<%} %>
          	<%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(19);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.RENTAL_CONSIGNMENT_REPORTS.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Rental / Consignment Reports
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-20">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Generate Rental PDF</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Generate Consignment PDF</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(19); //	Twentieth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %>
          </ul>
          </li>
           <%}%>
		 <%if(systatus.equalsIgnoreCase("INVENTORY")){ %>
        <%-- <li class="<%="Order Management".equals(pageTitle) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-truck"></i> <span>Order Management</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: none;">
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Purchase
          		</a>
          		<ul class="dropdown-menu menu-items"><li>
          			<div class="tab-pane tab-6">
          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Purchase</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
	          				<div class="col-md-12">
	          					<%
		          					htMenuItems = (java.util.Hashtable)menuListWithSequence.get(5);//	Sixth row
		          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
				                	iterator = htMenuItemDetail.keySet().iterator();
				                	while(iterator.hasNext()){
				                		String menuItemTitle = "" + iterator.next();
				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
						         %>
						                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
						               <%		                			
									}
								%>
	          				</div>
          				</div>
		              </div>
          		</li></ul>
          	</li>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales Estimate
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-7">
          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Sales Estimate</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
          		<div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(6); //	Seventh row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
          		</div>
				</div>                
              </div></li></ul>
          	</li>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-8">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Generate PDF</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">          		
          		<div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(7); //	Eighth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
          		</div>
			  </div></div>
              </li></ul>
          	</li>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Rental
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-9">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Rental</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(8); //	Ninth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
          		</div>
              </div></li></ul>
          	</li>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Transfer
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-10">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Transfer</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(9); //	Tenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
          		</div>
              </div></li></ul>
              </li>
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Force Close Orders
          		</a>
          		<ul class="dropdown-menu menu-items"><li><div class="tab-pane tab-11">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Force Close Orders</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(10); //	Eleventh row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
          		</div>
              </div></li>
              </ul>
          	</li>
          </ul>
        </li> --%>
        <%-- <li class="<%="Inbound Transaction".equals(pageTitle) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-truck"></i> <span>Purchase Transaction</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: none;">
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Purchase
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-12">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Purchase Reversal</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods Receipt</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Rental</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(11); //	Twelth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		</ul>
        </li> --%>
        
        <!--5 New Menu Changes (Azees 19.8.19) -->
        <li class="<%=IConstants.IN_HOUSE.equals(mainMenu) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-location-arrow"></i> <span>In House</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.IN_HOUSE.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(12);
	          if(htMenuItems.size()>0)
	          {
	          %>
          	<li class="dropdown">
          		<a href="#" class="<%=IConstants.IN_HOUSE_SUB_MENU.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			In House
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-12">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-4"><div class="col-md-12" style="width: 121%;"><strong class="menu-head">Stock Move / De-Kitting / Kitting</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Stock Take</strong></div></div>
	          				<!-- <div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Kitting / De-Kitting</strong></div></div> -->
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Generate Barcode </strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-4">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(12); //	Thirteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
								if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
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
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		 <%-- <div class="col-md-3">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div> --%>          		
          		<div class="col-md-4">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(4);//	Fourth column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                if (!menuItemURL.contains(".jsp")){
	                			%>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%	
		                		}else{
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		    
		                		}		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		<%} %>
          		</ul>
          
        </li>
         <%}%>
		 <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING") || systatus.equalsIgnoreCase("PAYROLL")){ %>
        <%-- <li class="<%="Outbound Transaction".equals(pageTitle) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-truck"></i> <span>Sales Transaction</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: none;">
          	<li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Sales
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-14">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Sales Reversal</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Goods Issue</strong></div></div>
	          				<div class="col-md-3"><div class="col-md-12"><strong class="menu-head">Rental / Transfer</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-3">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(13); //	Fourteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
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
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
          		</div>
          		</div>
              </div></li>
          		</ul>
          		</li>
          		</ul>
          
        </li> --%>
         
         <!--6 New Menu Changes (Azees 19.8.19) -->
         <li class="<%=IConstants.REPORTS.equals(mainMenu) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-file"></i> <span>Reports</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.REPORTS.equals(mainMenu) ? "block" : "none"%>;">
          <%htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14);
	          if(htMenuItems.size()>0)
	          {
	          %>
	          <li class="dropdown">
          		<a href="<%=rootURI%>/inventory/reports" class="<%=IConstants.INVENTORY.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Inventory
          		</a>          		
          		</li>
          	<%-- <li class="dropdown">
          		<a href="#" class="<%=IConstants.INVENTORY.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Inventory
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-27">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-6"><div class="col-md-12"><strong class="menu-head">Inventory Reports</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12" style="width:110%;"><strong class="menu-head">Inventory (Opening / Closing Stock) / Average Cost</strong></div></div>
	          				<div class="col-md-6"><div class="col-md-12" style="width:110%;"><strong class="menu-head">.</strong></div></div>
	          				<div class="col-md-4"><div class="col-md-12"><strong class="menu-head">Inventory Summary Alternate Brand Product</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-6">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(14); //	Fifteenth row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12" style="width:120%;"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
              </div>
              <div class="col-md-6">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(2);//	Second column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
				               <%		                			
							} %>
          		</div> --%>
          		<%-- <div class="col-md-4">
          		<%
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(3);//	Third column 
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
          		</div> --%>
          		<!-- </div>
              </div></li>
          		</ul>
          		</li> -->
          		<%} %>
          		<% if(ENABLE_POS.equals("1")){ %>
          		<%-- <li class="dropdown">
              		<a href="#" class="<%=IConstants.POS_REPORT.equals(subMenu) ? "active" : ""%>">
              			<i class="fa fa-angle-right pull-right"></i>
              			POS
              		</a>
          		<ul class="dropdown-menu menu-items"><li>
              			<div class="tab-pane tab-7">
              				<div class="row menu-head-row">
    	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">POS Reports</strong></div></div>
              				</div>
              				<div class="row">
              				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
              				</div>
              				<div class="row menu-link-row">
    	          				<div class="col-md-12">
    	          					<%
    		          					htMenuItems = (java.util.Hashtable)menuListWithSequence.get(71); //	Seventy two row
    		          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
    		          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
    				                	iterator = htMenuItemDetail.keySet().iterator();
    				                	while(iterator.hasNext()){
    				                		String menuItemTitle = "" + iterator.next();
    				                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
    						         if (!menuItemURL.contains(".jsp")){
    	                			%>
    				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
    				               <%	
    		                		}else{
    				                %>
    				                  <div class="col-md-12"><a class="menu-link" href="<%=rootURI%>/jsp/<%=menuItemURL%>"><%=menuItemTitle %></a></div>
    				               <%		    
    		                		}		                			
    									}
    								%>
    	          				</div>
              				</div>
    		              </div>
              		</li></ul>
          		</li> --%>
          		<%if(systatus.equalsIgnoreCase("INVENTORY")){ %>
          		<li class="dropdown">
          		<a href="<%=rootURI%>/posreports/reports" class="<%=IConstants.POS_REPORT.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			POS
          		</a>          		
          		</li>
          	<%} }%>
          	 <%if(systatus.equalsIgnoreCase("ACCOUNTING")){ %>	
          	<li class="dropdown">
          		<a href="<%=rootURI%>/accounting/reports" class="<%=IConstants.ACCOUNTING_SUB_MENU.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Accounting
          		</a>          		
          		</li>
          		<%} %>
          	<li class="dropdown">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(15); //	Twenty four row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.ACTIVITY_LOGS.equals(subMenu) ? "active" : ""%>">
          							<i class="fa fa-angle-right pull-right"></i>
          							<%=menuItemTitle %>
          						</a>	
				               <%		                			
							} %>
          		</li> 	
          	<%-- <li class="dropdown">
          		<a href="#">
          			<i class="fa fa-angle-right pull-right"></i>
          			Time Attendance
          		</a>
          		<ul class="dropdown-menu menu-items">
          		<li><div class="tab-pane tab-23">
          		          				<div class="row menu-head-row">
	          				<div class="col-md-12"><div class="col-md-12"><strong class="menu-head">Stock Take</strong></div></div>
          				</div>
          				<div class="row">
          				<hr style="margin-top: 0px; margin-bottom: 0px;"/>
          				</div>
          				<div class="row menu-link-row">
                <div class="col-md-12">
          		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(22); //	Twenty third row
          					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
          					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
		                	iterator = htMenuItemDetail.keySet().iterator();
		                	while(iterator.hasNext()){
		                		String menuItemTitle = "" + iterator.next();
		                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
				                %>
				                  <div class="col-md-12"><span class="menu-link" onclick="location.href='<%=rootURI%>/jsp/<%=menuItemURL%>'"><%=menuItemTitle %></span></div>
				               <%		                			
							} %>
              </div>
          		</div>
              </div></li>
          		</ul>
          		</li> --%>
          		</ul>
        </li>
         <%}%>
         
         <%if(systatus.equalsIgnoreCase("INVENTORY") || systatus.equalsIgnoreCase("ACCOUNTING")){ %>
        <li class="<%=IConstants.INTEGRATIONS.equals(mainMenu) ? "active" : ""%> treeview">
          <a href="#">
            <i class="fa fa-link"></i> <span>Integrations</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu" style="display: <%=IConstants.INTEGRATIONS.equals(mainMenu) ? "block" : "none"%>;">
          	<% htMenuItems = (java.util.Hashtable)menuListWithSequence.get(65);
          if(htMenuItems.size()>0)
          {%>
          <%if(systatus.equalsIgnoreCase("INVENTORY")){ %>
          <li class="dropdown">
        		<%	htMenuItems = (java.util.Hashtable)menuListWithSequence.get(65); //	Sixty sixth row
        					htMenuItemDetail = (java.util.Map)htMenuItems.get(1);//	First column
        					if (htMenuItemDetail == null){htMenuItemDetail = new java.util.LinkedHashMap();}//	Just to overcome NPE
                	iterator = htMenuItemDetail.keySet().iterator();
                	while(iterator.hasNext()){
                		String menuItemTitle = "" + iterator.next();
                		String menuItemURL = "" + htMenuItemDetail.get(menuItemTitle);
		                %>
		                <a href="<%=rootURI%>/<%=menuItemURL%>" class="<%=IConstants.PROJECT.equals(subMenu) ? "active" : ""%>">
		                	<%=menuItemTitle %>
        						  	<i class="fa fa-angle-right pull-right"></i>
		                </a>
		               <%		                			
					} %>
          		</li>
          <%} } %>
          <%if(ISPEPPOL.equalsIgnoreCase("1")){ %>
          <li class="dropdown">
          		<a href="<%=rootURI%>/integrations/peppolintegration" class="<%=IConstants.PEPPOL.equals(subMenu) ? "active" : ""%>">
          			<i class="fa fa-angle-right pull-right"></i>
          			Peppol Integration
          		</a>          		
          		</li>
	        <%}%>
          </ul>
        </li>
        <%}%>
      <!-- <span class="collapse-expand" data-ember-action="" data-ember-action-219="219">
      <a href="#" class="sidebar-toggle expand-sidebar" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      </span> -->
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
        <li class="active"><a href="<%=rootURI%>/home"><i class="fa fa-dashboard"></i> <%=pageTitle%></a></li>
        <%}else if ("Settings".equals(pageTitle)){ %>
        <li class="active"><a href="settings.jsp"><i class="fa fa-cog"></i> <%=pageTitle%></a></li>
        <%}%>
      </ol> --%>
    </section>
 <%} %>
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
				var urlStr =  "<%=rootURI%>/DashboardServlet";
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
		<%-- var content = '<div class="col-sm-4 text-center <%=invClass%>">'+
			'<img src="<%=rootURI%>/jsp/dist/img/ordermgt.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Inventory/Order Management</p>'+
		'</div>'+
	'<div class="col-sm-4 text-center <%=isAccClass%>">'+
		'<img src="<%=rootURI%>/jsp/dist/img/accounting.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Accounting</p>'+
	'</div>'+
	'<div class="col-sm-4 text-center <%=isPyrlClass%>">'+
		'<img src="<%=rootURI%>/jsp/dist/img/payroll.jpg" style="width: 100%;">'+
		'<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 10px;">Payroll</p>'+
	'</div>'; --%>
		var content = '';
		<%if(isInv.equalsIgnoreCase("1")){%>
			<%if(contextPath.contains("track")){%>
				content += '<a href= "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=INVENTORY">';
			<%}else{%>
			content += '<a href="/inventory/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=INVENTORY">';
			<%}%>
	    <%}else{%>
	    	content += '<a href="#">';
	    <%}%>
		content += '<div class="col-sm-4 text-center <%=invClass%>">';
		content += '<img src="<%=rootURI%>/jsp/dist/img/ordermgt.jpg" style="width: 100%;">';
		content += '<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Inventory/Order Management</p>';
		content += '</div>';
		content += '</a>';
		<%if(isAcc.equalsIgnoreCase("1")){%>
			<%if(contextPath.contains("track")){%>
				content += '<a href= "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=ACCOUNTING">';
			<%}else{%>
				content += '<a href="/accounting/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=ACCOUNTING">';
			<%}%>	    	
	    <%}else{%>
	    	content += '<a href="#">';
	    <%}%>
		content += '<div class="col-sm-4 text-center <%=isAccClass%>">';
		content += '<img src="<%=rootURI%>/jsp/dist/img/accounting.jpg" style="width: 100%;">';
		content += '<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 5px;">Accounting</p>';
		content += '</div>';
		content += '</a>';
		<%if(isPyrl.equalsIgnoreCase("1")){%>
			<%if(contextPath.contains("track")){%>
				content += '<a href= "<%=rootURI%>/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=PAYROLL">';
			<%}else{%>
				content += '<a href="/payroll/DashboardServlet?ACTION=LOGIN_TRANSFER&LOGPAGE=PAYROLL">';
			<%}%>
	    <%}else{%>
	    	content += '<a href="#">';
	    <%}%>
		content += '<div class="col-sm-4 text-center <%=isPyrlClass%>">';
		content += '<img src="<%=rootURI%>/jsp/dist/img/payroll.jpg" style="width: 100%;">';
		content += '<p class="category-name" style="color: #1d1d1d;font-size: 11px;font-weight: 400;margin-top: 10px;">Payroll</p>';
		content += '</div>';
		content += '</a>';
		
		$('[data-toggle="popover"]').popover({content: content, trigger: "focus", html: true, placement: "bottom"});
	});

	function getFromDateForPeriod(period){
		if (period == 'Last 30 days'){
			return moment().add(-30, 'days');//
		}else if(period == 'Last 7 days'){
			return moment().add(-7, 'days');//
		}else if(period == 'Last 15 days'){
			return moment().add(-15, 'days');//
		}else if (period == 'Today'){
			return moment();//
		}else if (period == 'Tomorrow'){
			return moment().add(1, 'days');//
		}else if (period == 'Yesterday'){
			return moment().add(-1, 'days');//
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
		}else if (period == 'Next 7 days'){
			return moment().add(1, 'days');//
		}else if (period == 'Next 15 days'){
			return moment().add(1, 'days');//
		}else if (period == 'Next 30 days'){
			return moment().add(1, 'days');//
		}else if (period == '&gt; 30 days'){
			return moment().add(30, 'days');//
		}
	}

	function getToDateForPeriod(period){
		if (period == 'Last 30 days'){
			return moment().add(-1, 'days');//
		}else if(period == 'Last 7 days'){
			return moment().add(-1, 'days');//
		}else if(period == 'Last 15 days'){
			return moment().add(-1, 'days');//
		}else if (period == 'Today'){
			return moment();//
		}else if (period == 'Yesterday'){
			return moment().add(-1, 'days');//
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
		}else if (period == 'Next 7 days'){
			return moment().add(7, 'days');
		}else if (period == 'Next 15 days'){
			return moment().add(15, 'days');
		}else if (period == 'Next 30 days'){
			return moment().add(30, 'days');
		}else if (period == '&gt; 30 days'){
			return getFormattedDate(moment(moment().add(99, 'year')));
		}
	}
	
	function navigateToMovementHistory(){
		document.forms['frmViewMovementHistory'].FROM_DATE.value=getFormattedDate(moment(), 'DD/MM/YYYY');
		document.forms['frmViewMovementHistory'].USERID.value='<%=LUSER%>';
		document.forms['frmViewMovementHistory'].submit();
	}
	
	var counter = '<%=AutoPopupCounter%>';
	var startcount = '<%=AutoPopupDelay%>';
	var enableOutletAutoPrintPopup = '<%=enableOutletAutoPrintPopup%>';  
	if(enableOutletAutoPrintPopup=="0")
		startcount = 0;
	var tt=setInterval(function(){startTime(startcount)},1000);

	function startTime(seconds)
	{
		if(counter>0){
		console.log(counter);
		console.log(seconds);
	    if(counter == seconds) {
	        clearInterval(tt);
        	checkstatus("PENDING");
	    } else {
		    if(seconds>=counter)
	        	counter++;
		    else
		    	counter=0;
	    }
	    document.getElementById("AUTOPOPUPCOUNTER").value = counter;
		}
	    
	}
	function checkstatus(status){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				STATUS : status,
				ORDERTYPE : '<%=AutoPopupOrderType%>',
				ACTION : "ORDER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					//console.log(data.status);
					if (data.status == "100") {
						/* var confm = confirm("Do you want print, new order?");
						if(confm)
							{							
							submitFormPrint("Print Pending Outbound Order");
							counter=0;
							setInterval(function(){startTime(30)},1000);
							} else {						
								counter=0;
								setInterval(function(){startTime(60)},1000);
								}	 */	
						counter=0;
						$("#confirmModal").modal();						
						return false;	
					} 
					else {
						counter=1;
						tt = setInterval(function(){startTime(<%=AutoPopupDelay%>)},1000);
						return false;
					}
				}
			});
		 return true;
}
	function submitFormPrint(actionvalue){
		var myWindow = window.open(	"/track/deleveryorderservlet?Submit="+actionvalue,'_blank' // <- This is what makes it open in a new window.
				);
		counter=1;
		clearInterval(tt);
		tt = setInterval(function(){startTime(<%=AutoPopupDelay%>)},1000);
		$("#confirmModal").modal('hide');
		
	}
	function cancelprint(){
		$("#confirmModal").modal('hide');
		counter=1;
		clearInterval(tt);
		tt = setInterval(function(){startTime(<%=AutoPopupDelay%>)},1000);
		}
</script>

<div id="confirmModal" class="modal fade" role="dialog" data-backdrop="static">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" onClick="cancelprint();" >&times;</button>
          		<h4 class="modal-title">Confirmation Box</h4>
        	</div>
        	<div class="modal-body">
        		<div class="container-fluid">
        		<p>New <%=AutoPopupOrderType%> order has been received. Do you want to print the order pick list? </p>
        		</div>
	        </div>
	        <div class="modal-footer">
	          <button type="button" id="btnPrintdd" onClick="submitFormPrint('Print Pending Outbound Order');" class="btn btn-success pull-left">Print</button>
	          <button type="button" onClick="cancelprint();" class="btn btn-warning pull-left">Cancel</button>
	        </div>
  		</div>
	</div>
</div>