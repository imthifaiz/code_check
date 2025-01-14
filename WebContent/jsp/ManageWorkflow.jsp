<!-- IMTI CREATED on 18-03-2022   -->
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%@page import="com.track.dao.PlantMstDAO"%>
<%
String title = "Manage Workflow";
String plant    = StrUtils.fString((String)session.getAttribute("PLANT"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>" />
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>

<script language="javascript">
	
 	function checkAll(isChk)
 	{
 		if ($('#selectall').is(':checked')) {
 			$('#pocreate').prop('checked',true);
 			$('#poupdate').prop('checked',true);
 			$('#podelete').prop('checked',true);
 			$('#porcreate').prop('checked',true);
 			$('#porupdate').prop('checked',true);
 			$('#pordelete').prop('checked',true);
 			$('#socreate').prop('checked',true);
 			$('#soupdate').prop('checked',true);
 			$('#sodelete').prop('checked',true);
 			$('#sorcreate').prop('checked',true);
 			$('#sorupdate').prop('checked',true);
 			$('#sordelete').prop('checked',true);
	    } else {
	    	$('#pocreate').prop('checked',false);
 			$('#poupdate').prop('checked',false);
 			$('#podelete').prop('checked',false);
 			$('#porcreate').prop('checked',false);
 			$('#porupdate').prop('checked',false);
 			$('#pordelete').prop('checked',false);
 			$('#socreate').prop('checked',false);
 			$('#soupdate').prop('checked',false);
 			$('#sodelete').prop('checked',false);
 			$('#sorcreate').prop('checked',false);
 			$('#sorupdate').prop('checked',false);
 			$('#sordelete').prop('checked',false);
	    }
 	}
 	
</script>
<%
StrUtils strUtils = new StrUtils();
String fieldDesc = "",smsg="",emsg="";
fieldDesc = StrUtils.fString(request.getParameter("result"));
// smsg = (String)request.getAttribute("sMsg");
// emsg = (String)request.getAttribute("eMsg");
%>
<center>
	<h2>
	<% if(!fieldDesc.equalsIgnoreCase("Unable To Update Manage Workflow")){%>
		<small class="success-msg"><%=fieldDesc%></small>
	<%}else{ %>
		<small class="error-msg"><%=fieldDesc%></small>
	<%} %>
	</h2>
</center>
<%-- <center>
	<h2><small class="success-msg fout"><%=smsg%></small></h2>
</center>
<center>
	<h2><small class="error-msg fout"><%=emsg%></small></h2>
</center> --%>
<div class="container-fluid m-t-20">
	<div class="box">
		             <ul class="breadcrumb backpageul">      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>       
                  <li><label>Manage Workflow</label></li>                                   
             </ul>   
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
				<div class="box-title pull-right">
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div> 
		</div>

		<div class="box-body">

			<FORM class="form-horizontal" name="form1" method="post">
				<INPUT type = "hidden" name="pid" value ="0">
				<INPUT type = "hidden" name="prid" value ="0">
				<INPUT type = "hidden" name="sid" value ="0">
				<INPUT type = "hidden" name="srid" value ="0">
				<input type="hidden" name="plant" id="plant" value="<%=plant%>" >
				<div class="row">
					<div class="col-12 col-sm-12">
						<label> <input type="checkbox" class="form-check-input"
							style="border: 0;" name="select" value="select" id="selectall"
							onclick="return checkAll(this.checked);"> &nbsp;
							Select/Unselect All &nbsp;
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-12 col-sm-12">
						<p><strong>Purchase Order</strong></p>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="pocreate" id="pocreate"> &nbsp;
								Create &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="poupdate" id="poupdate"> &nbsp;
								Edit &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="podelete" id="podelete"> &nbsp;
								Delete &nbsp;
							</label>
						</div>
					</div>
					<div class="col-12 col-sm-12">
						<p><strong>Purchase Order Return</strong></p>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="porcreate" id="porcreate"> &nbsp;
								Create &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="porupdate" id="porupdate"> &nbsp;
								Edit &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="pordelete" id="pordelete"> &nbsp;
								Delete &nbsp;
							</label>
						</div>
					</div>
					<div class="col-12 col-sm-12">
						<p><strong>Sales Order</strong></p>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="socreate" id="socreate"> &nbsp;
								Create &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="soupdate" id="soupdate"> &nbsp;
								Edit &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="sodelete" id="sodelete"> &nbsp;
								Delete &nbsp;
							</label>
						</div>
					</div>
					<div class="col-12 col-sm-12">
						<p><strong>Sales Order Return</strong></p>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="sorcreate" id="sorcreate"> &nbsp;
								Create &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="sorupdate" id="sorupdate"> &nbsp;
								Edit &nbsp;
							</label>
						</div>
						<div class="col-4 col-sm-4">
							<label> 
								<input type="checkbox" class="form-check-input" name="sordelete" id="sordelete"> &nbsp;
								Delete &nbsp;
							</label>
						</div>
					</div>
				</div>
			</FORM>
			<div class="form-group">
				<div class="col-sm-12" align="center">
					<button type="button" class="Submit btn btn-success aupdate" onClick="onAssign();">
						<b>Update</b>
					</button>
				</div>
			</div>

		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		var plants = document.form1.plant.value;
		setapprovalsettings(plants);
				$('.aupdate').show();
			});
	
	function onAssign() {

		var plant = document.form1.plant.value;
		if (plant == "" || plant == null) {
			alert("Please Enter/Select plant");
			return false;
		}

		var checkFound = false;

		if ($('#pocreate').is(':checked')) {
			checkFound = true;
		}
		if ($('#poupdate').is(':checked')) {
			checkFound = true;
		}
		if ($('#podelete').is(':checked')) {
			checkFound = true;
		}
		if ($('#porcreate').is(':checked')) {
			checkFound = true;
		}
		if ($('#porupdate').is(':checked')) {
			checkFound = true;
		}
		if ($('#pordelete').is(':checked')) {
			checkFound = true;
		}
		if ($('#socreate').is(':checked')) {
			checkFound = true;
		}
		if ($('#soupdate').is(':checked')) {
			checkFound = true;
		}
		if ($('#sodelete').is(':checked')) {
			checkFound = true;
		}
		if ($('#sorcreate').is(':checked')) {
			checkFound = true;
		}
		if ($('#sorupdate').is(':checked')) {
			checkFound = true;
		}
		if ($('#sordelete').is(':checked')) {
			checkFound = true;
		}

		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}

		document.form1.action = "../PlantApprovalSettings/update"
		document.form1.submit();
	}

	function setapprovalsettings(plant) {
		var urlStr = "/track/PlantApprovalSettings/getpltApproveMatrixbyplant";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				plant : plant
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "ok") {
					$.each(data.appmax, function( key, data ) {
						if(data.APPROVALTYPE == "PURCHASE"){
							if(data.ISCREATE == "1"){
								$('#pocreate').prop('checked',true);
							}else{
								$('#pocreate').prop('checked',false);
							}
							if(data.ISUPDATE == "1"){
								$('#poupdate').prop('checked',true);
							}else{
								$('#poupdate').prop('checked',false);
							}
							if(data.ISDELETE == "1"){
								$('#podelete').prop('checked',true);
							}else{
								$('#podelete').prop('checked',false);
							}

							$("input[name=pid]").val(data.ID);
						}
						if(data.APPROVALTYPE == "PURCHASE RETURN"){
				 			if(data.ISCREATE == "1"){
								$('#porcreate').prop('checked',true);
							}else{
								$('#porcreate').prop('checked',false);
							}
							if(data.ISUPDATE == "1"){
								$('#porupdate').prop('checked',true);
							}else{
								$('#porupdate').prop('checked',false);
							}
							if(data.ISDELETE == "1"){
								$('#pordelete').prop('checked',true);
							}else{
								$('#pordelete').prop('checked',false);
							}
							$("input[name=prid]").val(data.ID);
						}
						if(data.APPROVALTYPE == "SALES"){
							if(data.ISCREATE == "1"){
								$('#socreate').prop('checked',true);
							}else{
								$('#socreate').prop('checked',false);
							}
							if(data.ISUPDATE == "1"){
								$('#soupdate').prop('checked',true);
							}else{
								$('#soupdate').prop('checked',false);
							}
							if(data.ISDELETE == "1"){
								$('#sodelete').prop('checked',true);
							}else{
								$('#sodelete').prop('checked',false);
							}
							$("input[name=sid]").val(data.ID);
						}
						if(data.APPROVALTYPE == "SALES RETURN"){
							if(data.ISCREATE == "1"){
								$('#sorcreate').prop('checked',true);
							}else{
								$('#sorcreate').prop('checked',false);
							}
							if(data.ISUPDATE == "1"){
								$('#sorupdate').prop('checked',true);
							}else{
								$('#sorupdate').prop('checked',false);
							}
							if(data.ISDELETE == "1"){
								$('#sordelete').prop('checked',true);
							}else{
								$('#sordelete').prop('checked',false);
							}
							$("input[name=srid]").val(data.ID);
						}
					});

					$('.aupdate').show();
				} else {
					$('#pocreate').prop('checked',false);
		 			$('#poupdate').prop('checked',false);
		 			$('#podelete').prop('checked',false);
		 			$('#porcreate').prop('checked',false);
		 			$('#porupdate').prop('checked',false);
		 			$('#pordelete').prop('checked',false);
		 			$('#socreate').prop('checked',false);
		 			$('#soupdate').prop('checked',false);
		 			$('#sodelete').prop('checked',false);
		 			$('#sorcreate').prop('checked',false);
		 			$('#sorupdate').prop('checked',false);
		 			$('#sordelete').prop('checked',false);

		 			$("input[name=pid]").val("0");
		 			$("input[name=prid]").val("0");
		 			$("input[name=sid]").val("0");
		 			$("input[name=srid]").val("0");
					$('.aupdate').show();
				}
			}
		});	
	}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>