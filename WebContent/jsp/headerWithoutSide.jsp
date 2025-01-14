<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<jsp:useBean id="miscbean" class="com.track.gates.miscBean" />
<jsp:useBean id="ubean" class="com.track.gates.userBean" />
<% 
	String contextType = request.getParameter("context-type");
	contextType = (contextType == null ? "1" : contextType);
	String context = (contextType.equals("0") ? "jsp" : "../jsp");
	String rootURI = HttpUtils.getRootURI(request);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="-1">
  <%String pageTitle = request.getParameter("title"); %>
  <title>SUNPRO <%=pageTitle%></title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/ionicons.min.css">
  <!-- DataTables -->
  <%--<link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/dataTables.bootstrap.min.css"> 
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs/jszip-2.5.0/dt-1.10.16/b-1.5.1/b-colvis-1.5.1/b-html5-1.5.1/fh-3.1.3/r-2.2.1/rg-1.0.2/sl-1.2.5/datatables.min.css"/>
  --%>
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/lib/datatables.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/AdminLTE.min.css">
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
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- Google Font -->
  <link rel="stylesheet"
        href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/select2.min.css">
  <link rel="stylesheet" href="<%=rootURI%>/jsp/dist/css/main-style.css">
  <script>
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
</script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <header class="main-header">
    <!-- Logo -->
    
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top text-center no-margin">
      <!-- Sidebar toggle button-->
      <!-- <a href="#" class="login-logo"><img src="<%=rootURI%>/jsp/dist/img/Final-01.png"></a> -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <!-- Messages: style can be found in dropdown.less-->
        
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <img class="imgrounded-circle" src="<%=rootURI%>/jsp/images/trackNscan/nouser.png">
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class=""> <a class="#"> <%=session.getAttribute("LOGIN_USER").toString()%> </a> </li>
              <% 
              int companyCount = new PlantMstDAO().getCompanyCount(session.getAttribute("LOGIN_USER").toString());
              if (companyCount > 1){
              %>
              <li class=""> <a href="<%=rootURI%>/selectcompany"> Switch Company </a> </li>
              <%}%>
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
  <!-- Content Wrapper. Contains page content -->
