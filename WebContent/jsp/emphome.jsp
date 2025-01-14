<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>

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

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <header class="main-header">
  <%@include file="EmpSessionCheck.jsp"%>
  <%
	StrUtils StrUtils = new StrUtils();
  	String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
 	String empid = session.getAttribute("EMP_USER_ID").toString();
 	EmployeeDAO employeeDAO = new EmployeeDAO();
 	EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
 	HrHolidayMstDAO hrHolidayMstDAO = new HrHolidayMstDAO();
 	HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO =new HrLeaveApplyHdrDAO();
 	DateUtils dateutils = new DateUtils();
 	
 	String curtyear = dateutils.getYear();
  	ArrayList arrCust = employeeDAO.getEmployeeListbyid(empid,plant);
 	Map m=(Map)arrCust.get(0);
 	
 	String sEmpCode   = (String)m.get("EMPNO");
  	String sEmpName   = (String)m.get("FNAME");
 	String IMAGEPATH = (String)m.get("CATLOGPATH");
 	
 	List<EmployeeLeaveDETpojo> employeeleave = employeeLeaveDetDAO.EmployeeLeavedetlistpojo(plant, Integer.valueOf(empid), curtyear);
 	String sdate = "01/01/"+curtyear;
 	String edate = "31/12/"+curtyear;
 	List<HolidayMstPojo> holidaylist =  hrHolidayMstDAO.getHolidaybydate(plant, sdate, edate);
 	
 	List<LeaveApplyHdrPojo> appliedleavelist = hrLeaveApplyHdrDAO.getHrLeaveApplyHdrPOJObyEmpid(plant, Integer.valueOf(empid));
  %>

    <!-- Logo -->
    <a href="#"  class="logo">
      <!-- <img src="dist/img/Final-01.png" style="width: 60%;"> -->
      <img src="dist/img/logo-01.png" style="width: 60%;">
    </a>

    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
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
        <li class="empactive"><a href="#"><i class="fa fa-fw fa-users"></i> <span>Dashboard</span></a></li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  
  
  
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
    </section>
    <!-- Main content -->
    <section class="content">


      <!-- Main row -->
      <div class="row">
        <!-- Left col -->
        <div class="col-md-8">

          <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Applied Leave List</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="table-responsive">
                <table class="table no-margin" id="appliedleave">
                  <thead>
                  <tr>
                    <th>Leave Type</th>
                    <th>From Date</th>
                    <th>To Date</th>
                    <th>No of Days</th>
                    <th>Status</th>
                  </tr>
                  </thead>
                  <tbody>
                  <%for(LeaveApplyHdrPojo applyleave:appliedleavelist){ %>
	                  <tr>
	                  	<td><%=applyleave.getLEAVETYPE()%></td>
	                  	<td><%=applyleave.getFROM_DATE()%></td>
	                  	<td><%=applyleave.getTO_DATE()%></td>
	                  	<td><%=applyleave.getNUMBEROFDAYS()%></td>
	                  	<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
	                  	<td style="color:red;"><%=applyleave.getSTATUS()%></td>
	                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("approved")){ %>
	                  	<td style="color:green;"><%=applyleave.getSTATUS()%></td>
	                  	<%}else{ %>
	                  	<td><%=applyleave.getSTATUS()%></td>
	                  	 <%} %>
	                  </tr>
               	  <%} %>
                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>
            <!-- /.box-body -->
<!--             <div class="box-footer clearfix">
              <a href="javascript:void(0)" class="btn btn-sm btn-info btn-flat pull-left">Place New Order</a>
              <a href="javascript:void(0)" class="btn btn-sm btn-default btn-flat pull-right">View All Orders</a>
            </div> -->
            <!-- /.box-footer -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->

        <div class="col-md-4">
          <div class="box box-default">
            <div class="box-header with-border">
              <h3 class="box-title">Leave Balance</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
               			<%if(employeeleave.size() > 0){ %>
              	  		<div class="table-responsive">
                    		<table class="table no-margin" style="font-size: 90%;">
                      			<thead>
                  					<tr>
                    					<th>Leave Type</th>
					                    <th>Total Days</th>
					                    <th>Balance Days</th>
					                 </tr>
                  				</thead>
                  				<tbody>
                  				<%for(EmployeeLeaveDETpojo empleave:employeeleave){%>
				                   <tr>
				                   		<td><%=empleave.getLEAVETYPE() %></td>
				                    	<td><%=empleave.getTOTALENTITLEMENT() %></td>
				                    	<td><%=empleave.getLEAVEBALANCE() %></td>
				                  </tr>
				                <%} %>
								</tbody>
				             </table>
						</div>
						<%}else{ %>
						
						<%} %>
              <!-- /.row -->
            </div>

            <!-- /.footer -->
          </div>
          <!-- /.box -->

          <!-- PRODUCT LIST -->
          <div class="box box-primary">
            <div class="box-header with-border">
              <h3 class="box-title">Holiday List of <%=curtyear%></h3>

              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
				<div class="table-responsive">
                    <table id="holidaylist" class="table no-margin" style="font-size: 90%;">
                    			<thead>
                  					<tr>
                    					<th>Holiday Date</th>
					                    <th></th>
					                    <th>Description</th>
					                 </tr>
                  				</thead>
                  				<tbody>
                  		<% for(HolidayMstPojo hlist:holidaylist){%>
				              <tr>
				                 <td><%=hlist.getHOLIDAY_DATE()%></td>
				                 <td>-</td>	
				                 <td><%=hlist.getHOLIDAY_DESC()%></td>				              
				              </tr>
				        <%} %>
					    </tbody>
				   </table>
			   </div>
           </div>
            
            
            <!-- /.box-footer -->
          </div>
          
         
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

  <footer class="main-footer">
<!--     <div class="pull-right hidden-xs">
      <b>Version</b> 2.4.0
    </div>
    <strong>Copyright &copy; 2014-2016 <a href="https://adminlte.io">Almsaeed Studio</a>.</strong> All rights
    reserved. -->
  </footer>

  <!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
  <div class="control-sidebar-bg"></div>

</div>
<!-- ./wrapper -->

<!-- jQuery 3 -->
<script src="dist/js/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="dist/js/bootstrap.min.js"></script>

<script src="dist/js/jquery.dataTables.min.js"></script>
<script src="dist/js/dataTables.bootstrap.min.js"></script>
<!-- FastClick -->
<script src="dist/js/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>

<!-- SlimScroll -->
<script src="dist/js/jquery.slimscroll.min.js"></script>

<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>

	<script type="text/javascript">
	 $('#holidaylist').DataTable({
	      'paging'      : true,
	      'lengthChange': false,
	      'searching'   : false,
	      'ordering'    : false,
	      'info'        : false,
	      'autoWidth'   : false,
	      'bInfo'       : false,
	       pagingType: "simple"
	 })
	 
	 $('#appliedleave').DataTable({
	      'paging'      : true,
	      'lengthChange': true,
	      'searching'   : true,
	      'ordering'    : true,
	      'info'        : true,
	      'autoWidth'   : true
	 })
	</script>
</body>
</html>