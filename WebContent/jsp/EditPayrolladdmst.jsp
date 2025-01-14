<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.HrPayrollAdditionMst"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Edit Payroll Addition";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	HrPayrollAdditionMstDAO hrPayrollAdditionMstDAO = new HrPayrollAdditionMstDAO();
	HrPayrollAdditionMst hrPayrollAdditionMst = hrPayrollAdditionMstDAO.getpayrolladditionmstById(plant, Integer.valueOf(Id));
			
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_ADDITION%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
<%if(resultnew.equals("") || resultnew == null){}else{ %>
	  <div class="alert alert-danger alert-dismissible" style="width: max-content;margin:0 auto;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <%=resultnew %>
	  </div>
  <%} %>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <li><a href="../payroll/addsummary"><span class="underline-on-hover">Payroll Addition Summary</span> </a></li>                   
                <li><label>Edit Payroll Addition</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/addsummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form" action="/track/HrPayrollAdditionMstServlet?Submit=Update" method="post" onsubmit="return validatepayaddmst()">
				<input type="text" name="plant" value="<%=plant%>" hidden> 
				<input type="text" name="username" value=<%=username%> hidden> 
				<input type="text" name="pid" value="<%=Id%>" hidden>
				<input type="hidden" name="isrecoverable" value="<%=hrPayrollAdditionMst.getISDEDUCTION()%>">
				<input type="hidden" name="isclaim" value="<%=hrPayrollAdditionMst.getISCLAIM()%>">

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Addition Name</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="add_name" maxlength="100" readonly value="<%=hrPayrollAdditionMst.getADDITION_NAME()%>">
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Addition Description</label>
					<div class="col-sm-6">
						<input type="text" class="form-control emptype" name="add_description" value="<%=hrPayrollAdditionMst.getADDITION_DESCRIPTION()%>">						
					</div>
				</div>
				
				
				<%-- <div class="form-group">
					<label class="control-label col-form-label col-sm-3">Is Recoverable</label>
					<div class="col-sm-6">
						
						<input type="checkbox" id="recoverable" name="recoverable" onclick="setrecoverable(this)" <%if(hrPayrollAdditionMst.getISDEDUCTION() == 1){%>checked <%}%> >
					</div>
				</div> --%>
				
				
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-danger"  data-toggle="modal" data-target="#deleteaddition">Delete</button>
						<!-- <button type="button" class="btn btn-default"
							onclick="window.location.href='LeaveTypeSummary.jsp'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<div id="deleteaddition" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Deleted addition information cannot be retrieved. Are you sure about deleting ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmdelete" style="background:red;"><a href="/track/HrPayrollAdditionMstServlet?CMD=DELETE_ADDITION&ID=<%=Id%>" style="color: white;">
			        	Yes 
			         </a></button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<script>
	$(document).ready(function(){
		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 2000);
	});

	function setrecoverable(obj) {
		if ($(obj).is(":checked")) {
			$('input[name = "isrecoverable"]').val("1");
		} else {
			$('input[name = "isrecoverable"]').val("0");
		}
	}
	
	function validatepayaddmst(){
		
		var add_name=$('input[name = "add_name"]').val();

		if(add_name == ""){
			alert("Please enter addition name.");
			document.form.add_name.focus();
			return false;
		}

		return true;
	}
	
	function onClear()
	{
		document.form.add_name.value="";
		document.form.add_description.value="";
		$('#recoverable').prop('checked', false); 
	}
		
</script>
