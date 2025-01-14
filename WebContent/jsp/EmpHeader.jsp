<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<jsp:useBean id="ubean" class="com.track.gates.userBean" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Dashboard</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="dist/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="dist/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <link rel="stylesheet" href="dist/css/dataTables.bootstrap.min.css">
  <link rel="stylesheet" href="css/emp_main.css">
  
  <!-- jQuery 3 -->
	<script src="dist/js/jquery.min.js"></script>
	<!-- Bootstrap 3.3.7 -->
	<script src="dist/js/bootstrap.min.js"></script>
		<script src="dist/js/jquery-ui-1.12.1.js"></script>
	<%--<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> --%>
	<link rel="stylesheet" href="dist/css/jquery-ui-1.12.1.css">
	<script src="dist/js/jquery.dataTables.min.js"></script>
	  <link rel="stylesheet" href="dist/css/google-fonts.css">
  <link rel="stylesheet" href="dist/css/select2.min.css">
  <link rel="stylesheet" href="dist/css/bootstrap-toggle.min.css">
	<script src="dist/js/dataTables.bootstrap.min.js"></script>
	<script src="dist/js/bootstrap-toggle.min.js"></script>
	<script type="text/javascript" src="dist/js/moment.min.js"></script>
	<script src="js/typeahead.jquery.js"></script>
	<link rel="stylesheet" href="css/typeahead.css">
	

  <!-- <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic"> -->
  <style type="text/css">
  .responsive {
  width: 100%;
  max-width: 130px;
  height: auto;
}
  
  </style>
  
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <header class="main-header">
  <%
	StrUtils StrUtils = new StrUtils();
  	String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
 	String empid = session.getAttribute("EMP_USER_ID").toString();
 	EmployeeDAO employeeDAO = new EmployeeDAO();
 	
  	ArrayList arrCust = employeeDAO.getEmployeeListbyid(empid,plant);
 	Map m=(Map)arrCust.get(0);
 	
 	String sEmpCode   = (String)m.get("EMPNO");
  	String sEmpName   = (String)m.get("FNAME");
 	String IMAGEPATH = (String)m.get("CATLOGPATH");
 	
 	String companyname = ubean.getCompanyName(session.getAttribute("PLANT").toString()).toUpperCase();
 	
  %>

    <!-- Logo -->
    <a href="#"  class="logo">
    <!--   <img src="dist/img/Final-01.png" class="responsive"> -->
       <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><img src="dist/img/logo-xs.png" class="logo-xs" onclick="{location.href='EmpDashboard.jsp';}"></span>
      <!-- logo for regular state and mobile devices -->
      <!-- <span class="logo-lg"><img src="dist/img/Final-01.png" class="responsive" onclick="{location.href='EmpDashboard.jsp';}"></span> -->
      <span class="logo-lg"><img src="dist/img/logo-01.png" class="responsive" onclick="{location.href='EmpDashboard.jsp';}"></span>
    </a>

    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" style="color: #337ab7;" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <!-- Navbar Right Menu -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle">
              <img src="<%=(IMAGEPATH.equalsIgnoreCase("")) ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet?fileLocation="+IMAGEPATH%>" class="user-image" alt="User Image">
              <span class="hidden-xs"><%=sEmpName%></span>
            </a>
          </li>
          <!-- Control Sidebar Toggle Button -->
          <li data-original-title="Logout">
            <a href="Emplogout.jsp"><i class="fa fa-fw fa-power-off"></i></a>
          </li>
        </ul>
      </div>

    </nav>
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu" data-widget="tree">
        <li class="custmenu" id="dash_menu"><a href="EmpDashboard.jsp"><i class="fa fa-fw fa-users"></i> <span>Dashboard</span></a></li>
        <li class="custmenu" id="apleave"><a href="EmpLeaveApply.jsp"><i class="fa fa-fw fa-file-text-o"></i> <span>Apply Leave</span></a></li>
        <li class="custmenu" id="apclaim"><a href="EmpClaimApply.jsp"><i class="fa fa-fw fa-money"></i> <span>Apply Claim</span></a></li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  
    <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
		 <section class="content-header">
      <%-- <img class="dash-logo" style="width:20%;" src="<%=request.getContextPath() %>/GetCustomerLogoServlet"> --%>
      <span class="logo-lg"><img src="<%=request.getContextPath() %>/GetCustomerLogoServlet" class="responsive" onclick="{location.href='EmpDashboard.jsp';}"></span>
      <span style="margin-left: 15px;font-size: 15px;font-weight: bold;vertical-align: middle;"><%=companyname%></span>
      <%-- <ol class="breadcrumb">
      	<%if ("Home Page".equals(pageTitle)){ %>
        <li class="active"><a href="../home"><i class="fa fa-dashboard"></i> <%=pageTitle%></a></li>
        <%}else if ("Settings".equals(pageTitle)){ %>
        <li class="active"><a href="settings.jsp"><i class="fa fa-cog"></i> <%=pageTitle%></a></li>
        <%}%>
      </ol> --%>
    </section>
    </section>
  