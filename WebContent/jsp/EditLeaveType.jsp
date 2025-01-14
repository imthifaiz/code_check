<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.db.object.LeaveTypePojo"%>
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
	String title = "Edit Leave Type";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String fieldDesc = "";
	String Id = StrUtils.fString(request.getParameter("ID"));
	fieldDesc = StrUtils.fString(request.getParameter("result"));
	HrLeaveTypeDAO HrLeaveTypeDAO = new HrLeaveTypeDAO();
	LeaveTypePojo hrLeaveType = HrLeaveTypeDAO.getLeavetypeByIdpojo(plant, Integer.valueOf(Id));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="Leave Type" />
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
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../payroll/leavetype"><span class="underline-on-hover">Leave Type Summary</span> </a></li>   
                <li><label>Edit Leave Type</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/leavetype'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid" style="padding-bottom: 3%;">
			<input type="text" id="PageName" style="display: none;" value="">
			<form id="updateform" class="form-horizontal" name="form" action="/track/HrLeaveTypeServlet?Submit=Update" method="post" onsubmit="return validateleavetype()">
				<input type="text" name="plant" value="<%=plant%>" hidden> 
				<input type="text" name="username" value=<%=username%> hidden> 
				<input type="text" name="ltid" value="<%=Id%>" hidden>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Leave Type</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="leavetype" maxlength="100" value="<%=hrLeaveType.getLEAVETYPE()%>">
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Employee Type</label>
					<div class="col-sm-6">
						<input type="hidden" name="employeetypeid" value="<%=hrLeaveType.getEMPLOYEETYPEID()%>">
						<input type="text" class="form-control emptype" name="employeetype" value="<%=hrLeaveType.getEMPLOYEETYPE()%>">						
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Total Entitlement</label>
					<div class="col-sm-6">
						<input type="hidden"  name="tebackup" value="<%=hrLeaveType.getTOTALENTITLEMENT()%>">
						<input type="text" class="form-control totalentitlement" name="totalentitlement" value="<%=hrLeaveType.getTOTALENTITLEMENT()%>">
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3 required">Carry Forward</label>
					<div class="col-sm-6">
						<input type="hidden" name="cfbackup" value="<%=hrLeaveType.getCARRYFORWARD()%>">
						<input type="text" class="form-control carryforward" name="carryforward" value="<%=hrLeaveType.getCARRYFORWARD()%>">
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3">Notes</label>
					<div class="col-sm-6">
						<textarea  name="note" id="notes" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"><%=hrLeaveType.getNOTE()%></textarea>
						<%-- <input type="text" class="form-control" name="note" maxlength="800" value="<%=hrLeaveType.getNOTE()%>"> --%>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-3">Is No Pay Leave</label>
					<div class="col-sm-6">
						<input type="hidden" name="isnopayleave" value="<%=hrLeaveType.getISNOPAYLEAVE()%>">
						<input type="checkbox" id="nopayleave" name="nopayleave" onclick="setnoplayleave(this)" <%if(hrLeaveType.getISNOPAYLEAVE() == 1){%>checked <%}%> >
					</div>
				</div>
				
				
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
						<!-- <button type="button" class="btn btn-default"
							onclick="window.location.href='LeaveTypeSummary.jsp'">Cancel</button> -->
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<%@include file="Employeetypepopup.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<script>
	$(document).ready(function(){
		$('.emptype').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'EMPLOYEETYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/EmployeeTypeServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_EMPLOYEE_TYPE_DROPDOWN",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.EMPTYPELIST);
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
			    		return '<div onclick="setemployetprid(this,\''+data.ID+'\')"><p>' + data.EMPLOYEETYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
				menuElement.after( '<div class="accountAddBtn footer emptypepopup"  data-toggle="modal" data-target="#create_employee_type"><a href="#"> + New Employee Type</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
			$(document).on("focusout",".totalentitlement", function(){
				var value = $(this).val();
				var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
				var numbers = /^[0-9]+$/;
				if(value.match(decimal) || value.match(numbers)) 
				{ 
					var ldays=parseFloat(value).toFixed(1);
					$(this).val(ldays);
				}else{
					alert("Please enter valid total entitlement");
					var ldays=parseFloat("0").toFixed(1);
					$(this).val(ldays);
				}
			});
			
			$(document).on("focusout",".carryforward", function(){
				var value = $(this).val();
				var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
				var numbers = /^[0-9]+$/;
				if(value.match(decimal) || value.match(numbers)) 
				{ 
					var ldays=parseFloat(value).toFixed(1);
					$(this).val(ldays);
				}else{
					alert("Please enter valid carry forward");
					var ldays=parseFloat("0").toFixed(1);
					$(this).val(ldays);
				}
			});
			
			
			var checklop = "<%=hrLeaveType.getISNOPAYLEAVE()%>";
			if (checklop == "1"){
				$("input[name ='totalentitlement']").val("0.0");
				$("input[name ='carryforward']").val("0.0");
				$("input[name ='totalentitlement']").prop( "readonly", true );
				$("input[name ='carryforward']").prop( "readonly", true );
			}

	});
	
	function setemployetprid(obj, id){
		$('input[name = "employeetypeid"]').val(id);
	}
	
	function setnoplayleave(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='isnopayleave']").val("1");
			$("input[name ='totalentitlement']").val("0.0");
			$("input[name ='carryforward']").val("0.0");
			$("input[name ='totalentitlement']").prop( "readonly", true );
			$("input[name ='carryforward']").prop( "readonly", true );
		} else {
			$("input[name ='isnopayleave']").val("0");
			$("input[name ='totalentitlement']").val($("input[name ='tebackup']").val());
			$("input[name ='carryforward']").val($("input[name ='cfbackup']").val());
			$("input[name ='totalentitlement']").prop( "readonly", false );
			$("input[name ='carryforward']").prop( "readonly", false )
		}
	}
	
	function validateleavetype(){
		
		var leavetype=$('input[name = "leavetype"]').val();
		var employeetype=$('input[name = "employeetype"]').val();
		var totalentitlement=$('input[name = "totalentitlement"]').val();
		var carryforward=$('input[name = "carryforward"]').val();
			
		if(leavetype == ""){
			alert("Please enter leave type.");
			document.form.leavetype.focus();
			return false;
		}
		
		if(employeetype == ""){
			alert("Please enter employee type.");
			document.form.employeetype.focus();
			return false;
		}
		
		if(totalentitlement == ""){
			alert("Please enter total entitlement.");
			document.form.totalentitlement.focus();
			return false;
		}
		
		if(carryforward == ""){
			alert("Please enter carry forward.");
			document.form.carryforward.focus();
			return false;
		}
		
		return true;
	}
	
	function onClear()
	{
		document.form.leavetype.value="";
		document.form.employeetype.value="";
		document.form.totalentitlement.value="0.0";
		document.form.carryforward.value="0.0";
		document.form.isnopayleave.value="0";
		$("#notes").val("");
		$("input[name ='totalentitlement']").prop( "readonly", false );
		$("input[name ='carryforward']").prop( "readonly", false )
		$('#nopayleave').prop('checked', false); 
	}
		
</script>
