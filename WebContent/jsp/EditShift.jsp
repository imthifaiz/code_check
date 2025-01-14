<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.db.object.HrShiftMst"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Edit Shift";
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String msg = (String)request.getAttribute("Msg");
	HrShiftMst hrShiftMst = (HrShiftMst)request.getAttribute("HrShiftMst");
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Edit Shift" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Shift" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/clockpicker.js"></script>
<link rel="stylesheet" href="../jsp/css/clockpicker.css">
<style>
.extraInfo {
	border: 1px dashed #555;
	background-color: #f9f8f8;
	border-radius: 3px;
	color: #555;
	padding: 15px;
}

.offset-lg-7 {
	margin-left: 58.33333%;
}

#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}

</style>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                 <li><a href="../shift/summary"><span class="underline-on-hover">Shift Summary</span></a></li>	
                <li><label>Edit Shift</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../shift/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="box-body">
			<form class="form-horizontal" id="ShiftForm" name="form" method="post" 
			action="../shift/new" enctype="multipart/form-data" onsubmit="return validateShift()">
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Shift Name:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="SHIFTNAME" name="SHIFTNAME" maxlength="50"  value="<%=hrShiftMst.getSHIFTNAME()%>">
							<input type="hidden" name="SHIFTID" value="<%=hrShiftMst.getID()%>">
					</div>
				</div>
				
		<div class="form-group">
		<label class="control-label col-form-label col-sm-2"></label>
	 		<div class="col-sm-3">
		  		<label class="radio-inline"> <input type="radio" name="ISHOURBASED" id="ISHOURBASED" <%if (hrShiftMst.getISHOURBASED()==1) {%> checked <%}%> value="<%=hrShiftMst.getISHOURBASED()%>" onClick="onISHOURBASED();" >Is Hour Based</label>
				<label class="radio-inline"> <input type="radio" name="ISTIMEBASED" id="ISTIMEBASED" <%if (hrShiftMst.getISTIMEBASED()==1) {%> checked <%}%>  value="<%=hrShiftMst.getISTIMEBASED()%>" onClick="onISTIMEBASED();" >Is Time Based</label>
	     	</div>
		</div>
		
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Allocate Hour:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ALLOCATEHOUR" name="ALLOCATEHOUR" maxlength="5" placeholder="HH:MM"  value="<%=hrShiftMst.getALLOCATEHOUR()%>" readonly>
					</div>
		</div>
		
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">In Time:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="INTIME" name="INTIME" maxlength="5" placeholder="HH:MM"  value="<%=hrShiftMst.getINTIME()%>" readonly>
					</div>
		</div>
		
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Out Time:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="OUTTIME" name="OUTTIME" maxlength="5" placeholder="HH:MM"  value="<%=hrShiftMst.getOUTTIME()%>" readonly>
					</div>
		</div>
		
		<div class="form-group">
		<label class="control-label col-form-label col-sm-2"></label>
 		<div class="col-sm-3">
  		<label class="radio-inline"> <input type="radio" name="ACTIVE" value="Y" <%if (hrShiftMst.getIsActive().equals("Y")) {%> checked <%}%>>Active
										</label> 
		<label class="radio-inline"> <input type="radio"name="ACTIVE" value="N" <%if (hrShiftMst.getIsActive().equals("N")) {%> checked <%}%>>Non Active
										</label>
     	</div>
		</div>
		

				<div class="row form-group">      
					<div class="col-sm-offset-2 col-sm-9 txn-buttons">
						 <button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
$('#ALLOCATEHOUR').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#INTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
$('#OUTTIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});
</script>
<script type="text/javascript">

	$(document).ready(function(){
		if(document.getElementById("ISHOURBASED").value == "1")
			{
		$("#INTIME").attr("disabled", true);
		$("#OUTTIME").attr("disabled", true); 
			}
		if(document.getElementById("ISTIMEBASED").value == "1")
		{
			$("#ALLOCATEHOUR").attr("disabled", true);	 
		}
	});
	
	function onISHOURBASED()
	{
		if($('#ISHOURBASED').is(':checked')) {
			   
			   
			   document.getElementById("ISTIMEBASED").checked = false;
			   document.getElementById("ISHOURBASED").value = "1";
			   document.getElementById("ISTIMEBASED").value = "0";
			   
			   document.getElementById("INTIME").value = "";
			   document.getElementById("OUTTIME").value = "";
			   $("#ALLOCATEHOUR").attr("disabled", false);
			   $("#INTIME").attr("disabled", true);
			   $("#OUTTIME").attr("disabled", true);
		   }
	}
	
	function onISTIMEBASED()
	{
		if($('#ISTIMEBASED').is(':checked')) { 
			   document.getElementById("ISHOURBASED").checked = false;
			   document.getElementById("ISTIMEBASED").checked = true;
			   document.getElementById("ISHOURBASED").value = "0";
			   document.getElementById("ISTIMEBASED").value = "1";
			   
			   document.getElementById("ALLOCATEHOUR").value = "";
			   $("#ALLOCATEHOUR").attr("disabled", true);
			   $("#INTIME").attr("disabled", false);
			   $("#OUTTIME").attr("disabled", false);
		   }
	}
	
	function onClear()
	{
		document.form.SHIFTNAME.value="";
		document.form.ALLOCATEHOUR.value="";
		document.form.INTIME.value="";
		document.form.OUTTIME.value="";
		document.form.ISHOURBASED.checked=true;
		
		document.getElementById("ISTIMEBASED").checked = false;
		   document.getElementById("ISHOURBASED").value = "1";
		   document.getElementById("ISTIMEBASED").value = "0";
		   
		   document.getElementById("INTIME").value = "";
		   document.getElementById("OUTTIME").value = "";
		   $("#ALLOCATEHOUR").attr("disabled", false);
		   $("#INTIME").attr("disabled", true);
		   $("#OUTTIME").attr("disabled", true);
		 
	}
			function validateShift() {
				var SHIFTNAME   = document.form.SHIFTNAME.value;
				var ALLOCATEHOUR   = document.form.ALLOCATEHOUR.value;
				var INTIME   = document.form.INTIME.value;
				var OUTTIME   = document.form.OUTTIME.value;
				if(SHIFTNAME == "" || SHIFTNAME == null) {
					   alert("Please Enter Shift Name"); 
					   document.form.SHIFTNAME.focus();
					   return false; 
					   }
				if (document.form.ISHOURBASED.checked)
				{
					if(ALLOCATEHOUR == "" || ALLOCATEHOUR == null) {
						   alert("Please Enter Allocate Hour"); 
						   document.form.ALLOCATEHOUR.focus();
						   return false; 
						   }
				}
				if (document.form.ISTIMEBASED.checked)
				{
					if(INTIME == "" || INTIME == null) {
						   alert("Please Select In Time"); 
						   document.form.INTIME.focus();
						   return false; 
						   }
					
					if(OUTTIME == "" || OUTTIME == null) {
						   alert("Please Select Out Time"); 
						   document.form.OUTTIME.focus();
						   return false; 
						   }
					
				}
				var radio_choice = false;
				for (i = 0; i < document.form.ACTIVE.length; i++) {
					if (document.form.ACTIVE[i].checked)
						radio_choice = true;
				}
				if (!radio_choice) {
					alert("Please select Active or non Active mode.")
					return (false);
				}
				var formData = new FormData($('#ShiftForm')[0]);
			    $.ajax({
			        type: 'POST',
			        url: "../shift/edit",
				    data:  formData,
				    contentType: false,
				    processData: false,
			        success: function (data) {
			        	if(data.ERROR_CODE == 100)
			        		window.location.href="../shift/summary?msg="+data.MESSAGE;
			        	else
			        		alert(data.MESSAGE);
			        },
			        error: function (data) {
			            alert(data.responseText);
			        }
			    });
				
				return false;
			}
	</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="Edit Shift" />
</jsp:include>